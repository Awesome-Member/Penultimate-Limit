package Loader;

import java.lang.annotation.*;
import java.util.Collections;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import static net.bytebuddy.matcher.ElementMatchers.*;

public final class HookAdvice {
    private HookAdvice() { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface HookId { }

    static byte[] transform(Class<?> target, byte[] bytes) {
        ClassFileLocator locator = new ClassFileLocator.Compound(
                new ClassFileLocator.Simple(Collections.singletonMap(target.getName(), bytes)),
                ClassFileLocator.ForClassLoader.of(target.getClassLoader()));
        DynamicType.Builder<?> builder = new ByteBuddy().with(Implementation.Context.Disabled.Factory.INSTANCE)
                .with(InstrumentedType.Factory.Default.FROZEN)
                .redefine(new TypeDescription.ForLoadedType(target), locator);
        for (Hooks.Slot slot : Hooks.forClass(target)) {
            Class<?> exit = slot.method.getReturnType() == void.class ? VoidExit.class : ValueExit.class;
            builder = builder.visit(Advice.withCustomMapping().bind(HookId.class, slot.id)
                    .to(Enter.class, exit).on(named(slot.method.getName()).and(hasDescriptor(slot.descriptor))));
        }
        return builder.make().getBytes();
    }

    public static final class Enter {
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static boolean enter(@HookId int id,
                @Advice.This(optional = true) Object target,
                @Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
                @Advice.Local("hookCall") Hooks.Invocation call) {
            call = Hooks.enter(id, target, args);
            if (call == null) return false;
            args = call.args;
            return call.cancelled;
        }
    }

    public static final class ValueExit {
        @Advice.OnMethodExit
        public static void exit(@Advice.Local("hookCall") Hooks.Invocation call,
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object result) {
            if (call != null) result = Hooks.exit(call, call.cancelled ? call.result : result);
        }
    }

    public static final class VoidExit {
        @Advice.OnMethodExit
        public static void exit(@Advice.Local("hookCall") Hooks.Invocation call) {
            if (call != null) Hooks.exit(call, null);
        }
    }
}
