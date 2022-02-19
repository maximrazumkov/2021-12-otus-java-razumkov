package ru.otus.aop.instrumentation.proxy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

class MethodAnnotationScanner extends MethodVisitor {
    private final Map<String, String> methodsWithParams;
    private final String methodName;
    private final String descriptor;

    public MethodAnnotationScanner(Map<String, String> methodsWithParams, String methodName, String descriptor) {
        super(Opcodes.ASM5);
        this.methodsWithParams = methodsWithParams;
        this.methodName = methodName;
        this.descriptor = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("visitAnnotation: desc=" + desc + " visible=" + visible);
        if ("Lru/otus/aop/instrumentation/proxy/Log;".equals(desc)) {
            String descriptor = this.descriptor == null ? "" : this.descriptor;
            this.methodsWithParams.put(this.methodName, descriptor);
        }
        return super.visitAnnotation(desc, visible);
    }


}
