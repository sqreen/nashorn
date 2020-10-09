package io.sqreen.nashorn.agent;

import jdk.internal.reflect.CallerSensitive;
import jdk.internal.reflect.Reflection;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

class Premain {
    public static void premain(String agentArgs, Instrumentation inst) {
        ElementMatcher.Junction<MethodDescription> checkPackageAccess =
                named("checkPackageAccess").and(takesArguments(String.class));
        ResettableClassFileTransformer resettableClassFileTransformer = new AgentBuilder.Default()
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(named("java.lang.SecurityManager"))
                .transform((DynamicType.Builder<?> builder, TypeDescription type, ClassLoader classLoader, JavaModule module) ->
                        builder
                                .visit(new MemberAttributeExtension.ForMethod()
                                        .annotateMethod(new CallerSensitive() {
                                            @Override
                                            public Class<? extends Annotation> annotationType() {
                                                return CallerSensitive.class;
                                            }
                                        })
                                        .on(checkPackageAccess))
                                .method(checkPackageAccess)
                                .intercept(Advice.to(SkipChecksAdvice.class))
                )
                .installOn(inst);
        System.out.println(resettableClassFileTransformer);
    }

    public static class SkipChecksAdvice {
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class /* skip body on non-null */)
        static Object before(@Advice.Argument(0) String className) {
            Class<?> callerClass = Reflection.getCallerClass();
            if (!"jdk.internal.loader.ClassLoaders$AppClassLoader".equals(callerClass.getName())) {
                return null;
            }
            if ("io.sqreen.nashorn.internal".equals(className)) {
                return "skip";
            }
            return null;
        }
    }
}
