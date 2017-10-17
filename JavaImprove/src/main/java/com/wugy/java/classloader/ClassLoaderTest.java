package com.wugy.java.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * wugy on 2017/10/17 10:26
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream inStream = getClass().getResourceAsStream(fileName);
                if (null == inStream)
                    return super.loadClass(name);
                try {
                    byte[] b = new byte[inStream.available()];
                    inStream.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException();
                }
            }
        };

        Object obj = myLoader.loadClass("java.lang.Object").newInstance();
        System.out.println(obj instanceof java.lang.Object);
        //        Object obj = myLoader.loadClass("com.wugy.java.classloader.ClassLoaderTest").newInstance();
        //        System.out.println(obj instanceof com.wugy.java.classloader.ClassLoaderTest);
        //        两个类是不同类加载器加载
    }
}
