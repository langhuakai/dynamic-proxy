package com.wei.proxy.myproxy;

import org.springframework.util.FileCopyUtils;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class MyProxy {

    private static final String rt = "\r";

    public static Object newProxyInstance(MyClassLoader loader, Class<?> interfaces, MyInvocationHandler handler)
            throws IllegalArgumentException {
        if (handler == null) {
            throw new NullPointerException();
        }
        Method[] methods = interfaces.getMethods();
        StringBuffer proxyClassString = new StringBuffer();
        proxyClassString.append("package ")
                .append(loader.getProxyClassPackage()).append(";").append(rt)
                .append("import java.lang.reflect.Method;").append(rt)
                .append("public class $MyProxy0 implements ").append(interfaces.getName()).append("{").append(rt)
                .append("MyInvocationHandler handler;").append(rt)
                .append("public $MyProxy0(MyInvocationHandler handler) {").append(rt)
                .append("this.handler = handler;}").append(rt)
                .append(getMethodString(methods, interfaces)).append("}");
        String fileName = loader.getDir() + File.separator + "$MyProxy0.java";
        File myProxyFile = new File(fileName);
        try {
            compile(proxyClassString, myProxyFile);
            Class $myProxy0 = loader.findClass("$MyProxy0");
            Constructor constructor = $myProxy0.getConstructor(MyInvocationHandler.class);
            Object o = constructor.newInstance(handler);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void compile(StringBuffer proxyClassString, File myProxyFile) throws IOException {
        FileCopyUtils.copy(proxyClassString.toString().getBytes(), myProxyFile);
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
        Iterable javaFileObjects = standardJavaFileManager.getJavaFileObjects(myProxyFile);
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardJavaFileManager, null, null, null, javaFileObjects);
        task.call();
        standardJavaFileManager.close();
    }

    private static String getMethodString(Method[] methods, Class interfaces) {
        StringBuffer methodStringBuffer = new StringBuffer();
        for (Method method : methods) {
            methodStringBuffer.append("public void ").append(method.getName())
                    .append("()").append(" throws Throwable {")
                    .append("Method method1 = ").append(interfaces.getName())
                    .append(".class.getMethod(\"").append(method.getName())
                    .append("\", new Class[] {});")
                    .append("this.handler.invoke(this, method1, null);}").append(rt);
        }
        return methodStringBuffer.toString();
    }
}
