package bridge.scripting;

import Loader.HookAgent;
import Loader.Hooks;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;
import org.objectweb.asm.Type;

public final class LuaRuntime {
    public static final LuaRuntime INSTANCE = new LuaRuntime();
    private final ExecutorService workers = Executors.newCachedThreadPool(task -> {
        Thread thread = new Thread(task, "other-lua"); thread.setDaemon(true); return thread;
    });
    private volatile Context current;

    private LuaRuntime() { }

    public synchronized void start(String source, String name, Consumer<String> output) {
        stop();
        Context context = new Context(output); current = context;
        workers.execute(() -> context.run(source, name));
    }

    public synchronized void stop() {
        Context context = current; current = null;
        if (context != null) context.stop("Script stopped.");
    }

    public boolean isRunning() { Context context = current; return context != null && context.active.get(); }
    public boolean hasAgent() { return HookAgent.isAvailable(); }

    public static final class GameJavaLib extends LuajavaLib {
        @Override protected Class<?> classForName(String name) throws ClassNotFoundException {
            return Class.forName(name, true, LuaRuntime.class.getClassLoader());
        }
    }

    private static final class Abort extends Error {
        Abort(String message) { super(message); }
    }

    private static final class Budget {
        final long deadline;
        int instructions;
        Budget(long milliseconds) { deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(milliseconds); }
    }

    private static final class Context {
        final AtomicBoolean active = new AtomicBoolean(true);
        final ReentrantLock lock = new ReentrantLock();
        final List<Hooks.Handle> subscriptions = new ArrayList<>();
        final ThreadLocal<Budget> budget = new ThreadLocal<>();
        final Consumer<String> output;
        volatile Thread worker;
        long logWindow;
        int logCount;

        Context(Consumer<String> output) { this.output = output; }

        synchronized void log(String text) {
            long now = System.nanoTime();
            if (now - logWindow >= TimeUnit.SECONDS.toNanos(1)) { logWindow = now; logCount = 0; }
            if (++logCount <= 100) output.accept(text.length() > 4000 ? text.substring(0, 4000) + "…" : text);
        }

        void stop(String reason) {
            if (!active.getAndSet(false)) return;
            synchronized (subscriptions) { for (Hooks.Handle handle : subscriptions) handle.close(); subscriptions.clear(); }
            Thread thread = worker;
            if (thread != null && thread != Thread.currentThread()) thread.interrupt();
            output.accept(reason);
        }

        void track(Hooks.Handle handle) {
            synchronized (subscriptions) { if (active.get()) subscriptions.add(handle); else handle.close(); }
        }

        void run(String source, String name) {
            worker = Thread.currentThread();
            worker.setContextClassLoader(LuaRuntime.class.getClassLoader());
            lock.lock();
            try {
                if (!active.get()) return;
                Globals globals = JsePlatform.standardGlobals();
                globals.load(new GameJavaLib());
                globals.load(new DebugLib() {
                    @Override public void onInstruction(int pc, Varargs args, int top) {
                        Budget limit = budget.get();
                        if (!active.get()) throw new Abort("Script stopped.");
                        if (limit == null) throw new Abort("Lua ran outside a managed callback.");
                        if (++limit.instructions > 1000000 || (limit.instructions & 255) == 0 && System.nanoTime() > limit.deadline)
                            throw new Abort("Lua execution limit exceeded.");
                        super.onInstruction(pc, args, top);
                    }
                });
                globals.set("debug", LuaValue.NIL);
                globals.set("coroutine", LuaValue.NIL);
                globals.get("package").get("loaded").set("coroutine", LuaValue.NIL);
                globals.set("print", new VarArgFunction() {
                    @Override public Varargs invoke(Varargs args) {
                        StringBuilder text = new StringBuilder();
                        for (int i = 1; i <= args.narg(); i++) { if (i > 1) text.append('\t'); text.append(args.arg(i).tojstring()); }
                        log(text.toString()); return LuaValue.NONE;
                    }
                });
                LuaTable script = new LuaTable();
                script.set("stop", new ZeroArgFunction() {
                    @Override public LuaValue call() { stop("Script stopped. Hooks removed."); return LuaValue.NIL; }
                });
                globals.set("script", script);
                globals.set("java", javaBridge(globals.get("luajava").get("bindClass")));
                LuaTable hooks = new LuaTable();
                hooks.set("before", hookFunction(true)); hooks.set("after", hookFunction(false)); globals.set("hooks", hooks);
                budget.set(new Budget(1000));
                globals.load(source, name).call();
                if (active.get()) {
                    synchronized (subscriptions) {
                        if (subscriptions.isEmpty()) stop("Script completed.");
                        else log("Script running. Active hooks: " + subscriptions.size());
                    }
                }
            } catch (VirtualMachineError | ThreadDeath fatal) { stop("Fatal JVM error."); throw fatal; }
            catch (Throwable exception) { if (active.get()) stop("Script error: " + exception); }
            finally { budget.remove(); worker = null; lock.unlock(); Thread.interrupted(); }
        }

        private LuaValue hookFunction(boolean before) {
            return new VarArgFunction() {
                @Override public Varargs invoke(Varargs args) {
                    if (!active.get()) throw new Abort("Script stopped.");
                    try {
                        Class<?> target = args.arg1().isstring() ? Class.forName(args.arg1().checkjstring(), true,
                                LuaRuntime.class.getClassLoader()) : (Class<?>)args.arg1().checkuserdata(Class.class);
                        String signature = args.arg(2).checkjstring();
                        Method method = findMethod(target, signature, false);
                        LuaValue callback = args.arg(3).checkfunction();
                        Hooks.Callback handler = call -> invokeCallback(callback, call, method.getParameterTypes());
                        Hooks.Handle handle = before ? Hooks.before(target, signature, handler) : Hooks.after(target, signature, handler);
                        track(handle);
                        LuaTable token = new LuaTable();
                        token.set("remove", new ZeroArgFunction() {
                            @Override public LuaValue call() {
                                handle.close(); synchronized (subscriptions) { subscriptions.remove(handle); } return LuaValue.NIL;
                            }
                        });
                        return token;
                    } catch (ReflectiveOperationException | RuntimeException exception) { throw new LuaError(exception); }
                }
            };
        }

        private void invokeCallback(LuaValue callback, Hooks.Invocation call, Class<?>[] parameters) {
            if (!active.get() || !lock.tryLock()) return;
            Budget previous = budget.get();
            try {
                if (!active.get()) return;
                if (previous == null) budget.set(new Budget(50));
                LuaTable context = new LuaTable(), arguments = new LuaTable();
                LuaValue[] originalArguments = new LuaValue[call.args.length];
                LuaValue originalResult = CoerceJavaToLua.coerce(call.result);
                context.set("target", CoerceJavaToLua.coerce(call.target));
                context.set("args", arguments); context.set("result", originalResult);
                boolean[] cancelled = {false};
                context.set("cancel", new VarArgFunction() {
                    @Override public Varargs invoke(Varargs args) {
                        LuaValue value = args.arg1() == context ? args.arg(2) : args.arg1();
                        context.set("result", value); cancelled[0] = true; return LuaValue.NONE;
                    }
                });
                for (int i = 0; i < call.args.length; i++) {
                    originalArguments[i] = CoerceJavaToLua.coerce(call.args[i]); arguments.set(i + 1, originalArguments[i]);
                }
                callback.call(context);
                if (!active.get()) return;
                Object[] updated = new Object[parameters.length];
                for (int i = 0; i < updated.length; i++) updated[i] = arguments.get(i + 1).raweq(originalArguments[i])
                        ? call.args[i] : CoerceLuaToJava.coerce(arguments.get(i + 1), parameters[i]);
                Object result = call.returnType == void.class ? null : !cancelled[0] && context.get("result").raweq(originalResult)
                        ? call.result : CoerceLuaToJava.coerce(context.get("result"), call.returnType);
                System.arraycopy(updated, 0, call.args, 0, updated.length);
                call.result = result;
                if (cancelled[0]) call.cancel(result);
            } catch (VirtualMachineError | ThreadDeath fatal) { stop("Fatal JVM error."); throw fatal; }
            catch (Throwable exception) { stop("Hook error: " + exception); }
            finally { if (previous == null) budget.remove(); else budget.set(previous); lock.unlock(); }
        }
    }

    private static LuaTable javaBridge(LuaValue bindClass) {
        LuaTable java = new LuaTable(); java.set("class", bindClass);
        java.set("get", new VarArgFunction() {
            @Override public Varargs invoke(Varargs args) {
                try {
                    Object target = args.arg1().checkuserdata();
                    return CoerceJavaToLua.coerce(findField(target, args.arg(2).checkjstring()).get(target instanceof Class ? null : target));
                } catch (ReflectiveOperationException | RuntimeException exception) { throw new LuaError(exception); }
            }
        });
        java.set("set", new VarArgFunction() {
            @Override public Varargs invoke(Varargs args) {
                try {
                    Object target = args.arg1().checkuserdata(); Field field = findField(target, args.arg(2).checkjstring());
                    field.set(target instanceof Class ? null : target, CoerceLuaToJava.coerce(args.arg(3), field.getType()));
                    return LuaValue.NONE;
                } catch (ReflectiveOperationException | RuntimeException exception) { throw new LuaError(exception); }
            }
        });
        java.set("call", new VarArgFunction() {
            @Override public Varargs invoke(Varargs args) {
                try {
                    Object target = args.arg1().checkuserdata();
                    Method method = findMethod(target instanceof Class ? (Class<?>)target : target.getClass(), args.arg(2).checkjstring(), true);
                    Class<?>[] types = method.getParameterTypes();
                    if (args.narg() != types.length + 2) throw new IllegalArgumentException("Wrong argument count");
                    Object[] values = new Object[types.length];
                    for (int i = 0; i < types.length; i++) values[i] = CoerceLuaToJava.coerce(args.arg(i + 3), types[i]);
                    method.setAccessible(true);
                    return CoerceJavaToLua.coerce(method.invoke(target instanceof Class ? null : target, values));
                } catch (ReflectiveOperationException | RuntimeException exception) { throw new LuaError(exception); }
            }
        });
        return java;
    }

    private static Field findField(Object target, String name) throws NoSuchFieldException {
        for (Class<?> type = target instanceof Class ? (Class<?>)target : target.getClass(); type != null; type = type.getSuperclass()) {
            try { Field field = type.getDeclaredField(name); field.setAccessible(true); return field; }
            catch (NoSuchFieldException ignored) { }
        }
        throw new NoSuchFieldException(name);
    }

    private static Method findMethod(Class<?> target, String signature, boolean inherited) throws NoSuchMethodException {
        for (Class<?> type = target; type != null; type = inherited ? type.getSuperclass() : null)
            for (Method method : type.getDeclaredMethods())
                if ((method.getName() + Type.getMethodDescriptor(method)).equals(signature)) return method;
        throw new NoSuchMethodException(target.getName() + "." + signature);
    }
}
