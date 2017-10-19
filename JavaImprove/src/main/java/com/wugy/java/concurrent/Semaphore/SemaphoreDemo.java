package com.wugy.java.concurrent.Semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore：正常的锁（来自concurrent.locks或内建的synchronized锁）在任何时刻都只允许一个任务访问一项资源，计数信号量允许n个
 * 任务同时访问该资源。
 * <p>
 * wugy on 2017/10/19 11:36
 */
public class SemaphoreDemo {

    private static final int SIZE = 25;

    public static void main(String[] args) throws Exception {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < SIZE; i++)
            exec.execute(new CheckOutTask<>(pool));
        System.out.println("All CheckoutTasks created");

        List<Fat> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            Fat fat = pool.checkOut();
            System.out.println(i + " main thread checked out");
            fat.operation();
            list.add(fat);
        }
        Future<?> blocked = exec.submit(() -> {
            try {
                pool.checkOut();
            } catch (InterruptedException e) {
                System.out.println("checkOut() interrupted");
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + list);
        list.forEach(fat -> pool.checkIn(fat));
        list.forEach(fat -> pool.checkIn(fat));
        exec.shutdown();
    }
}
