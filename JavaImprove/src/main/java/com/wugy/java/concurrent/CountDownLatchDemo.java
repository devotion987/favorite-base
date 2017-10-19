package com.wugy.java.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch：被用来同步一个或多个任务，强制它们等待由其它任务执行的一组任务完成
 * <p>
 * wugy on 2017/10/18 15:06
 */
public class CountDownLatchDemo {

    private static final int SIZE = 100;

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(SIZE);
        for (int i = 0; i < 10; i++)
            service.execute(new WaitTask(latch));

        for (int i = 0; i < SIZE; i++)
            service.execute(new TaskPortion(latch));

        System.out.println("Launched all tasks");
        service.shutdown();
    }
}

class WaitTask implements Runnable {

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

class TaskPortion implements Runnable {

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