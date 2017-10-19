package com.wugy.java.concurrent.PriorityBlockingQueue;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * wugy on 2017/10/19 10:47
 */
public class PrioritizedTaskConsumer implements Runnable {

    private PriorityBlockingQueue<Runnable> queue;

    public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted())
                queue.take().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish PrioritizedTaskConsumer");
    }
}
