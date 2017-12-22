package com.wugy.java.proxy;

import javassist.*;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;

/**
 * devotion on 2017-02-09 8:35.
 */
public class DynamicProxyNatureTest {

    @Test
    public void testDynamicNature() throws Exception {
        CountService delegate = new CountServiceImpl();

        long time = System.currentTimeMillis();
        CountService jdkProxy = createJdkProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Proxy: " + time);

        time = System.currentTimeMillis();
        CountService cglibProxy = createCglibProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create CGLIB Proxy: " + time);

        time = System.currentTimeMillis();
        CountService javassistProxy = createJavassistProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Proxy: " + time);

        time = System.currentTimeMillis();
        CountService javassistByteCodeProxy = createJavassistByteCodeProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Bytecode Proxy: " + time);

        time = System.currentTimeMillis();
        CountService asmByteCodeProxy = createAsmByteCodeProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create ASM Proxy: " + time);

        System.out.println("=============");

        for (int i = 0; i < 3; i++) {
            test(jdkProxy, "Run JDK Proxy: ");
            test(cglibProxy, "Run CGLIB Proxy: ");
            test(javassistProxy, "Run JAVAASSIST Proxy: ");
            test(javassistByteCodeProxy, "Run JAVAASSIST Bytecode Proxy: ");
            test(asmByteCodeProxy, "Run ASM Bytecode Proxy: ");
            System.out.println("=============");
        }
    }

    private void test(CountService delegate, String label) {
        delegate.count(); // warm up  
        int count = 10000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            delegate.count();
        }
        time = System.currentTimeMillis() - time;
        System.out.println(label + time + " ms, " + new DecimalFormat().format(count * 1000 / time) + " t/s");

    }

    private CountService createJdkProxy(CountService delegate) {
        return (CountService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{CountService.class},
                new JdkHandler(delegate));
    }

    private static class JdkHandler implements InvocationHandler {

        final Object delegate;

        JdkHandler(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(delegate, args);
        }
    }

    private CountService createCglibProxy(CountService delegate) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterceptor(delegate));
        enhancer.setInterfaces(new Class[]{CountService.class});
        return (CountService) enhancer.create();
    }

    private static class CglibInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return methodProxy.invoke(delegate, objects);
        }
    }

    private CountService createJavassistProxy(CountService delegate) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{CountService.class});
        Class proxyClazz = proxyFactory.createClass();
        CountService javassistProxy = (CountService) proxyClazz.newInstance();
        ((ProxyObject) javassistProxy).setHandler(new JavassistInterceptor(delegate));
        return javassistProxy;
    }

    private static class JavassistInterceptor implements MethodHandler {

        final Object delegate;

        JavassistInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            return thisMethod.invoke(delegate, args);
        }
    }

    private CountService createJavassistByteCodeProxy(CountService delegate) throws Exception {
        ClassPool classPool = new ClassPool(true);

        String className = CountService.class.getName();
        CtClass ctClass = classPool.makeClass(className + "JavaassistProxy");
        ctClass.addInterface(classPool.get(className));
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
        ctClass.addField(CtField.make("public " + className + " delegate;", ctClass));
        ctClass.addMethod(CtMethod.make("public int count() {return delegate.count();}", ctClass));
        Class clazz = ctClass.toClass();
        CountService byteCodeProxy = (CountService) clazz.newInstance();
        Field field = byteCodeProxy.getClass().getField("delegate");
        field.set(byteCodeProxy, delegate);
        return byteCodeProxy;
    }

    private CountService createAsmByteCodeProxy(CountService delegate) throws Exception {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        String clazzName = CountService.class.getName();
        String className = clazzName + "AsmProxy";
        String classPath = className.replace(".", "/");
        String interfacePath = clazzName.replace(".", "/");
        classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, classPath, null, "java/lang/Object", new String[]{interfacePath});

        MethodVisitor initVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        initVisitor.visitCode();
        initVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        initVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        initVisitor.visitInsn(Opcodes.RETURN);
        initVisitor.visitMaxs(0, 0);
        initVisitor.visitEnd();

        FieldVisitor fieldVisitor = classWriter.visitField(Opcodes.ACC_PUBLIC, "delegate", "L" + interfacePath + ";", null, null);
        fieldVisitor.visitEnd();

        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "count", "()I", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, classPath, "delegate", "L" + interfacePath + ";");
        methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, interfacePath, "count", "()I");
        methodVisitor.visitInsn(Opcodes.IRETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        classWriter.visitEnd();
        byte[] code = classWriter.toByteArray();
        CountService byteCodeProxy = (CountService) new ByteArrayClassLoader().getClass(className, code).newInstance();
        Field filed = byteCodeProxy.getClass().getField("delegate");
        filed.set(byteCodeProxy, delegate);
        return byteCodeProxy;
    }

    private static class ByteArrayClassLoader extends ClassLoader {

        ByteArrayClassLoader() {
            super(ByteArrayClassLoader.class.getClassLoader());
        }

        public synchronized Class getClass(String name, byte[] code) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
            return defineClass(name, code, 0, code.length);
        }

    }

}
