package com.wugy.java.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * devotion on 2017-01-21 09:14
 */
public class AtomicStudy {

    private int count = 1;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private void count() {
        count++;
    }

    private void safeCount() {
        for (; ; ) {
            int i = atomicInteger.get();
            boolean success = atomicInteger.compareAndSet(i, ++i);
            if (success)
                break;
        }
    }

    @Test
    public void testAtomic() {
//        AtomicStudy test = new AtomicStudy();
        List<Thread> threads = new ArrayList<>(600);
        long start = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            Thread t = new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    count();
                    safeCount();
                }
            });
            threads.add(t);
        }
        threads.forEach(t -> t.run());
        //所有线程执行完
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(count);
        System.out.println(atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }


}
