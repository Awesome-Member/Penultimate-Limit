package Loader;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public final class HookAgent {
    private static volatile Instrumentation instrumentation;
    private static final ThreadLocal<Throwable> failure = new ThreadLocal<>();

    private HookAgent() { }

    public static void premain(String arguments, Instrumentation instance) { install(instance); }
    public static void agentmain(String arguments, Instrumentation instance) { install(instance); }

    private static synchronized void install(Instrumentation instance) {
        if (instrumentation != null) return;
        if (!instance.isRetransformClassesSupported()) throw new IllegalStateException("JVM does not support method hooks");
        instance.addTransformer(new ClassFileTransformer() {
            @Override public byte[] transform(ClassLoader loader, String name, Class<?> redefining,
                    ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
                if (redefining == null || !Hooks.hasHooks(redefining)) return null;
                try { return HookAdvice.transform(redefining, bytes); }
                catch (Throwable exception) {
                    failure.set(exception);
                    throw new IllegalClassFormatException(exception.toString());
                }
            }
        }, true);
        instrumentation = instance;
    }

    public static boolean isAvailable() { return instrumentation != null; }

    static void retransform(Class<?> target) throws Exception {
        Instrumentation instance = instrumentation;
        if (instance == null) throw new IllegalStateException("Start Java with -javaagent:target/Penultimate-Limit.jar");
        if (!instance.isModifiableClass(target)) throw new IllegalArgumentException("Class cannot be modified: " + target.getName());
        failure.remove();
        try {
            instance.retransformClasses(target);
            Throwable problem = failure.get();
            if (problem != null) throw new IllegalStateException("Cannot instrument " + target.getName(), problem);
        } finally { failure.remove(); }
    }
}
