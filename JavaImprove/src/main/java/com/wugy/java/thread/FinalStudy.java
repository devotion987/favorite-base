package com.wugy.java.thread;

import org.junit.Test;

/**
 * devotion on 2017-02-05 14:17
 */
public class FinalStudy {

    private int i;
    private final int j;
    private FinalStudy finalStudy;

    public FinalStudy() {
        i = 1;
        j = 2;
    }

    private void writer() {
        finalStudy = new FinalStudy();
    }

    public void reader() {
        FinalStudy obj = this.finalStudy;
        System.out.println("i = " + obj.i);
        System.out.println("j = " + obj.j);
    }


    @Test
    public void testFinal() {
        Thread t1 = new Thread(() -> writer());
        Thread t2 = new Thread(() -> reader());
        t1.run();
        t2.run();
    }
}

