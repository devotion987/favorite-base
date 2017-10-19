package com.wugy.java.concurrent.PriorityBlockingQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue：优先级队列，队列中的对象按照优先级顺序处理任务，队列中没有元素时将直接阻塞读取者
 * <p>
 * wugy on 2017/10/19 10:49
 */
public class PriorityBlockingQueueDemo {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();
        exec.execute(new PrioritizedTaskProducer(queue, exec));
        exec.execute(new PrioritizedTaskConsumer(queue));
    }
}
