package com.wugy.java.concurrent.CountDownLatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * wugy on 2017/10/18 14:56
 */
public class TaskPortion implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;

    public TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
            System.out.println(this + " completed");
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "TaskPortion{" + "id=" + id + '}';
    }
}
