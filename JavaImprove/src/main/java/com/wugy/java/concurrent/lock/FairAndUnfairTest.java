package com.wugy.java.concurrent.lock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {

    private static ReentrantLock2 fairLock = new ReentrantLock2(true);
    private static ReentrantLock2 unfairLock = new ReentrantLock2(false);

    @Test
    public void fair() {
        testLock(fairLock);
    }

    @Test
    public void unfair() {
        testLock(unfairLock);
    }

    private void testLock(ReentrantLock2 lock) {
        for (int i = 0; i < 5; i++) {
            new Job(lock, i + "").start();
        }
    }

    private static class Job extends Thread {
        private ReentrantLock2 lock;

        Job(ReentrantLock2 lock, String name) {
            super(name);
            this.lock = lock;
        }

        @Override
        public void run() {
            // 连续2次打印当前的Thread和等待队列中的Thread（略）
            try {
                System.out.print(String.format("Lock by[%s],", this.getName()));
                lock.lock();
                System.out.println(String.format("Waiting by%s", lock.getQueuedThreads()));
            } finally {
                lock.unlock();
            }
        }
    }

    private static class ReentrantLock2 extends ReentrantLock {
        ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}