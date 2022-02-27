package ru.otus.aop.instrumentation.proxy;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                return addProxyMethod(className, classfileBuffer);
            }
        });

    }

    private static byte[] addProxyMethod(String className, byte[] originalClass) {
        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        final Map<String, String> methodsWithParams = new HashMap();
        ClassVisitor scannerAnnotationLog = new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodAnnotationScanner(methodsWithParams, name, descriptor);
            }
        };

        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("visitMethod: access=" + access + " name=" + name + " desc=" + descriptor + " signature=" + signature + " exceptions=" + exceptions);
                if (methodsWithParams.containsKey(name)) {
                    var newName = name + "Log";
                    return super.visitMethod(access, newName, descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(scannerAnnotationLog, Opcodes.ASM5);
        cr.accept(cv, Opcodes.ASM5);

        for (var method : methodsWithParams.entrySet()) {
            MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getKey(), method.getValue(), null, null);

            var handle = new Handle(
                    H_INVOKESTATIC,
                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants",
                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                    false);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            String[] parameters = method.getValue().split(";");
            StringBuilder methodArguments = new StringBuilder("Executed method: ");
            int idx = method.getValue().lastIndexOf(")") + 1;
            String descriptor = method.getValue().substring(0, idx);
            methodArguments.append(method.getKey());
            for (int i = 1; i <= parameters.length - 1; ++i) {
                methodArguments.append(", param").append(i).append(" = \u0001");
                mv.visitVarInsn(Opcodes.ALOAD, i);
            }
            mv.visitInvokeDynamicInsn("makeConcatWithConstants", descriptor + "Ljava/lang/String;", handle, methodArguments.toString());

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            for (int i = 0; i < parameters.length; ++i) {
                mv.visitVarInsn(Opcodes.ALOAD, i);
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, method.getKey() + "Log", method.getValue(), false);

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }
}
