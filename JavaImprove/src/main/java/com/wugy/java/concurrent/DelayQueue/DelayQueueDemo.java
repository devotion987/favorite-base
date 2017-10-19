package com.wugy.java.concurrent.DelayQueue;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DelayQueue是一个无界BlockingQueue，用于放置实现了{@link java.util.concurrent.Delayed}接口的对象，其中的对象只能在其到期时
 * 才能从队列中取走。这种队列是有序的，即对头对象的延迟到期时间最长。如果没有任何延迟到期，那么就不会有任何头元素，并且poll()将返回null
 * <p>
 * wugy on 2017/10/19 9:54
 */
public class DelayQueueDemo {

    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService service = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(rand.nextInt(1000)));
        }
        queue.add(new DelayedTask.EndSentinel(1000, service));
        service.execute(new DelayedTaskConsumer(queue));

    }
}
