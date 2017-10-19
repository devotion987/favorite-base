package com.wugy.java.concurrent.DelayQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * wugy on 2017/10/19 9:30
 */
public class DelayedTask implements Runnable, Delayed {

    private static int count = 0;
    private final int id = count++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliSeconds) {
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

    public String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {

        private ExecutorService service;

        public EndSentinel(int delayInMilliSeconds, ExecutorService service) {
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
