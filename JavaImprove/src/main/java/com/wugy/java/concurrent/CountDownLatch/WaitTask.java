package com.wugy.java.concurrent.CountDownLatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * wugy on 2017/10/18 15:01
 */
public class WaitTask implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch latch;

    public WaitTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await();
            System.out.println("Latch barrier passed for " + this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(this + " interrupted");
        }
    }

    @Override
    public String toString() {
        return "WaitTask{" + "id=" + id + '}';
    }
}
