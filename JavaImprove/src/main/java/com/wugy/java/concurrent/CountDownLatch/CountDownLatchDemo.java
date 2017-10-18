package com.wugy.java.concurrent.CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
