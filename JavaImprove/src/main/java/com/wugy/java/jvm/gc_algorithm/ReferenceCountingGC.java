package com.wugy.java.jvm.gc_algorithm;

/**
 * 给对象中添加一个引用计数器，每当有一个地方引用它时，计数器值就加1；当引用失效时，计数器值就减1；任何时刻计数器为0的对象就是不可能再被使用的。
 * 但当两个对象相互引用，导致它们的引用计数都不为0，于是引用计数算法无法通知GC收集器回收它们。
 * <p>
 * wugy on 2017/10/19 13:45
 */
public class ReferenceCountingGC {

    private Object instance = null;
    private static int _1MB = 1024 * 1024;

    /**
     * 这个成员属性的唯一意义就是占点内存，以便能在GC日志中看清楚是否被回收过
     */
    private byte[] bigSize = new byte[2 * _1MB];

    private static void testGc() {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        //假设在这行发生GC，objA和objB是否能被回收？
        System.gc();
    }

    public static void main(String[] args) {
        testGc();
    }
}
