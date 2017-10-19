package com.wugy.java.concurrent.PriorityBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * wugy on 2017/10/19 10:13
 */
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask> {

    private Random rand = new Random(47);
    private static int count = 0;
    private final int id = count++;
    private final int priority;
    protected static List<PrioritizedTask> sequence = new ArrayList<>();

    public PrioritizedTask(int priority) {
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return Integer.compare(priority, o.priority);
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "PrioritizedTask{" + "id=" + id + ", priority=" + priority + '}';
    }

    public String summary() {
        return "(" + id + ":" + priority + ")";
    }

    public static class EndSentinel extends PrioritizedTask {

        private ExecutorService exec;

        public EndSentinel(ExecutorService exec) {
            super(-1);
            this.exec = exec;
        }

        @Override
        public void run() {
            int newCount = 0;
            for (PrioritizedTask pt : sequence) {
                System.out.println(pt.summary());
                if (++newCount % 5 ==0)
                    System.out.println();
            }
            System.out.println();
            System.out.println(this + " Calling shutdown");
            exec.shutdownNow();
        }
    }
}
