package com.wugy.java.concurrent.DelayQueue;

import java.util.concurrent.DelayQueue;

/**
 * wugy on 2017/10/19 9:50
 */
public class DelayedTaskConsumer implements Runnable{

    private DelayQueue<DelayedTask> queue;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted())
                queue.take().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finish DelayedTaskConsumer");
    }
}
