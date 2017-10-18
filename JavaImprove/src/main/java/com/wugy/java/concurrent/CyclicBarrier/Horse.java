package com.wugy.java.concurrent.CyclicBarrier;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * wugy on 2017/10/18 15:22
 */
public class Horse implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private int strides = 0;
    private final CyclicBarrier barrier;

    public Horse(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    strides += rand.nextInt(3);
                }
                barrier.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Horse{" + "id=" + id + '}';
    }

    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) s.append("*");
        s.append(id);
        return s.toString();
    }
}
