package com.wugy.java.thread;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * devotion on 2017-02-20 16:56.
 */
public class ConditionTest {

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() throws InterruptedException {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testCondition() throws Exception {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                conditionWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                conditionSignal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }
}
