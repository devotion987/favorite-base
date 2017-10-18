package com.wugy.java.thread;

import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

public class CyclicBarrierTest {

    private CyclicBarrier c = new CyclicBarrier(3);

    @Test
    public void testCyclicBarrier() {
        new Thread(() -> {
            try {
                c.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(1);
        }).start();
        try {
            c.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }
}