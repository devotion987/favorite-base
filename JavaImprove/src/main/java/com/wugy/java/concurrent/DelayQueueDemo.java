package com.wugy.java.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * DelayQueue是一个无界BlockingQueue，用于放置实现了{@link java.util.concurrent.Delayed}接口的对象，其中的对象
 * 只能在其到期时才能从队列中取走。这种队列是有序的，即对头对象的延迟到期时间最长。如果没有任何延迟到期，那么就不会有任何
 * 头元素，并且poll()将返回null
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

class DelayedTaskConsumer implements Runnable {

    private DelayQueue<DelayedTask> queue;

    DelayedTaskConsumer(DelayQueue<DelayedTask> queue) {
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

class DelayedTask implements Runnable, Delayed {

    private static int count = 0;
    private final int id = count++;
    private final int delta;
    private final long trigger;
    static List<DelayedTask> sequence = new ArrayList<>();

    DelayedTask(int delayInMilliSeconds) {
        delta = delayInMilliSeconds;
        trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
        sequence.add(this);
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
//        if (trigger < that.trigger) return -1;
//        if (trigger > that.trigger) return 1;
//        return 0;
        return Long.compare(trigger, that.trigger);
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    @Override
    public String toString() {
        return "DelayedTask{" + "id=" + id + ", delta=" + delta + ", trigger=" + trigger + '}';
    }

    String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {

        private ExecutorService service;

        EndSentinel(int delayInMilliSeconds, ExecutorService service) {
            super(delayInMilliSeconds);
            this.service = service;
        }

        @Override
        public void run() {
            sequence.forEach(delayedTask -> System.out.println(delayedTask.summary()));
            System.out.println();
            System.out.println(this + " Calling shutdown");
            service.shutdownNow();
        }
    }
}
