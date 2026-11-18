package Loader;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.objectweb.asm.Type;

public final class Hooks {
    public interface Callback { void invoke(Invocation call) throws Exception; }

    public static final class Invocation {
        public final Object target;
        public final Object[] args;
        public final Class<?> returnType;
        public Object result;
        public boolean cancelled;
        private final Slot slot;
        private Invocation(Slot slot, Object target, Object[] args) {
            this.slot = slot; this.target = target; this.args = args; returnType = slot.method.getReturnType();
        }
        public void cancel(Object value) { result = convert(value, returnType); cancelled = true; }
    }

    public static final class Handle implements AutoCloseable {
        private final List<Handle> list;
        private final Slot slot;
        private final Callback callback;
        private volatile boolean active = true;
        private Handle(Slot slot, List<Handle> list, Callback callback) { this.slot = slot; this.list = list; this.callback = callback; }
        @Override public void close() { remove(this); }
    }

    static final class Slot {
        final int id;
        final Method method;
        final String descriptor;
        final List<Handle> before = new CopyOnWriteArrayList<>(), after = new CopyOnWriteArrayList<>();
        Slot(int id, Method method) { this.id = id; this.method = method; descriptor = Type.getMethodDescriptor(method); }
    }

    private static final Map<Class<?>, List<Slot>> classes = new ConcurrentHashMap<>();
    private static final Map<Integer, Slot> slots = new ConcurrentHashMap<>();
    private static final AtomicInteger sequence = new AtomicInteger();
    private static final ThreadLocal<Set<Integer>> dispatching = ThreadLocal.withInitial(HashSet::new);

    private Hooks() { }

    public static Handle before(Class<?> target, String signature, Callback callback) {
        return add(target, signature, callback, true);
    }
    public static Handle after(Class<?> target, String signature, Callback callback) {
        return add(target, signature, callback, false);
    }

    private static synchronized Handle add(Class<?> target, String signature, Callback callback, boolean before) {
        Objects.requireNonNull(target); Objects.requireNonNull(callback);
        if (!HookAgent.isAvailable()) throw new IllegalStateException("Agent is not connected: -javaagent:target/Penultimate-Limit.jar");
        if (target.getClassLoader() == null || target.getName().startsWith("Loader.")
                || target.getName().startsWith("java.") || target.getName().startsWith("javax.")
                || target.getName().startsWith("org.objectweb.asm.") || target.getName().startsWith("net.bytebuddy."))
            throw new IllegalArgumentException("Hooks are not supported for " + target.getName());
        Method found = null;
        for (Method method : target.getDeclaredMethods()) {
            if ((method.getName() + Type.getMethodDescriptor(method)).equals(signature)) { found = method; break; }
        }
        if (found == null) throw new IllegalArgumentException("Declared method not found: " + target.getName() + "." + signature);
        if (Modifier.isNative(found.getModifiers()) || Modifier.isAbstract(found.getModifiers()))
            throw new IllegalArgumentException("A concrete Java method is required: " + signature);
        List<Slot> classSlots = classes.computeIfAbsent(target, key -> new CopyOnWriteArrayList<>());
        Slot slot = null;
        for (Slot candidate : classSlots) if (candidate.method.equals(found)) { slot = candidate; break; }
        boolean fresh = slot == null;
        if (fresh) {
            slot = new Slot(sequence.incrementAndGet(), found); classSlots.add(slot); slots.put(slot.id, slot);
        }
        List<Handle> list = before ? slot.before : slot.after;
        Handle handle = new Handle(slot, list, callback); list.add(handle);
        if (fresh) try { HookAgent.retransform(target); }
        catch (Exception | LinkageError exception) {
            handle.active = false; list.remove(handle); classSlots.remove(slot); slots.remove(slot.id);
            if (classSlots.isEmpty()) classes.remove(target, classSlots);
            throw new IllegalStateException("Cannot install hook: " + target.getName() + "." + signature, exception);
        }
        return handle;
    }

    private static synchronized void remove(Handle handle) {
        if (!handle.active) return;
        handle.active = false; handle.list.remove(handle);
        Slot slot = handle.slot;
        if (!slot.before.isEmpty() || !slot.after.isEmpty()) return;
        Class<?> target = slot.method.getDeclaringClass();
        List<Slot> remaining = classes.get(target);
        if (remaining != null) {
            remaining.remove(slot);
            if (remaining.isEmpty()) classes.remove(target, remaining);
        }
        slots.remove(slot.id);
        try { HookAgent.retransform(target); }
        catch (Exception | LinkageError exception) {
            System.err.println("[Hooks] Callbacks removed, but class restoration failed for " + target.getName() + ": " + exception);
        }
    }

    static boolean hasHooks(Class<?> target) { List<Slot> list = classes.get(target); return list != null && !list.isEmpty(); }
    static List<Slot> forClass(Class<?> target) { return classes.get(target); }

    public static Invocation enter(int id, Object target, Object[] args) {
        Slot slot = slots.get(id);
        if (slot == null || slot.before.isEmpty() && slot.after.isEmpty() || dispatching.get().contains(id)) return null;
        Invocation call = new Invocation(slot, target, args);
        invoke(slot.before, call, true);
        return call;
    }

    public static Object exit(Invocation call, Object result) {
        if (call == null) return result;
        call.result = result;
        invoke(call.slot.after, call, false);
        return call.result;
    }

    private static void invoke(List<Handle> callbacks, Invocation call, boolean before) {
        Set<Integer> active = dispatching.get(); active.add(call.slot.id);
        try {
            for (Handle handle : callbacks) {
                if (!handle.active) continue;
                Object[] previousArgs = call.args.clone(); Object previousResult = call.result; boolean cancelled = call.cancelled;
                try {
                    handle.callback.invoke(call);
                    Class<?>[] types = call.slot.method.getParameterTypes();
                    if (before) for (int i = 0; i < types.length; i++) call.args[i] = convert(call.args[i], types[i]);
                    if (!before || call.cancelled) call.result = convert(call.result, call.returnType);
                } catch (VirtualMachineError | ThreadDeath fatal) { throw fatal; }
                catch (Throwable exception) {
                    System.arraycopy(previousArgs, 0, call.args, 0, previousArgs.length);
                    call.result = previousResult; call.cancelled = cancelled; handle.close();
                    System.err.println("[Hooks] Disabled callback for " + call.slot.method + ": " + exception);
                }
            }
        } finally { active.remove(call.slot.id); if (active.isEmpty()) dispatching.remove(); }
    }

    private static Object convert(Object value, Class<?> type) {
        if (type == void.class) return null;
        if (!type.isPrimitive()) {
            if (value == null || type.isInstance(value)) return value;
        } else if (value instanceof Number) {
            Number number = (Number)value;
            if (type == byte.class) return number.byteValue(); if (type == short.class) return number.shortValue();
            if (type == int.class) return number.intValue(); if (type == long.class) return number.longValue();
            if (type == float.class) return number.floatValue(); if (type == double.class) return number.doubleValue();
            if (type == char.class) return (char)number.intValue();
        } else if (type == boolean.class && value instanceof Boolean || type == char.class && value instanceof Character) return value;
        throw new IllegalArgumentException("Expected " + type.getName() + ", got " + value);
    }
}
