package com.wugy.java.proxy;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * devotion on 2017-02-09 21:36
 */
public class Programmer {

    public void code() {
        System.out.println("I am a programmer, Just coding...");
    }

    private static class MyClassLoader extends ClassLoader {

        Class<?> defineMyClass(byte[] b, int off, int len) {
            return super.defineClass(null, b, off, len);
        }
    }

    @Test
    public void testByteCode() throws Exception {
        File file = new File(".");

        String canonicalPath = file.getCanonicalPath();
        System.out.println("canonicalPath = " + canonicalPath);
        InputStream inputStream = new FileInputStream(canonicalPath +
                "\\target\\classes\\com\\wugy\\java\\proxy\\Programmer.class");

        byte[] result = new byte[1024];
        int count = inputStream.read(result);

        Class<?> clazz = new MyClassLoader().defineMyClass(result, 0, count);
        System.out.println(clazz.getCanonicalName());

        Object obj = clazz.newInstance();
        clazz.getMethod("code", null).invoke(obj, null);
    }
}
