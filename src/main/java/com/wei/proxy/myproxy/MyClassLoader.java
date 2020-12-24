package com.wei.proxy.myproxy;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    private File dir;
    private String proxyClassPackage;

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public String getProxyClassPackage() {
        return proxyClassPackage;
    }

    public void setProxyClassPackage(String proxyClassPackage) {
        this.proxyClassPackage = proxyClassPackage;
    }

    public MyClassLoader(String path, String proxyClassPackage) {
        this.dir = new File(path);
        this.proxyClassPackage = proxyClassPackage;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (dir != null) {
            File classFile = new File(dir, name + ".class");
            if (classFile.exists()) {
                try {
                    byte[] classBytes = FileCopyUtils.copyToByteArray(classFile);
                    return defineClass(proxyClassPackage + "." + name, classBytes, 0, classBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.findClass(name);
    }
}
