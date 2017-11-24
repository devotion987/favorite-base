package com.wugy.java.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Semaphore：正常的锁（来自concurrent.locks或内建的synchronized锁）在任何时刻都只允许一个任务访问一项资源，
 * 计数信号量允许n个任务同时访问该资源。
 * <p>
 * wugy on 2017/10/19 11:36
 */
public class SemaphoreDemo {

    private static final int SIZE = 25;

    public static void main(String[] args) throws Exception {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < SIZE; i++)
            exec.execute(new CheckOutTask<>(pool));
        System.out.println("All CheckoutTasks created");

        List<Fat> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            Fat fat = pool.checkOut();
            System.out.println(i + " main thread checked out");
            fat.operation();
            list.add(fat);
        }
        Future<?> blocked = exec.submit(() -> {
            try {
                pool.checkOut();
            } catch (InterruptedException e) {
                System.out.println("checkOut() interrupted");
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + list);
        list.forEach(fat -> pool.checkIn(fat));
        list.forEach(fat -> pool.checkIn(fat));
        exec.shutdown();
    }
}

class Fat {

    private static int count = 0;
    private final int id = count++;

    @Override
    public String toString() {
        return "Fat{" + "id=" + id + '}';
    }

    void operation() {
        System.out.println(this);
    }
}

class CheckOutTask<T> implements Runnable {

    private static int count = 0;
    private final int id = count++;
    private Pool<T> pool;

    CheckOutTask(Pool<T> pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            T item = pool.checkOut();
            System.out.println(this + " checked out " + item);
            TimeUnit.MILLISECONDS.sleep(1);
            System.out.println(this + " checking in " + item);
            pool.checkIn(item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CheckOutTask{" + "id=" + id + '}';
    }
}

class Pool<T> {

    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore available;

    Pool(Class<T> classObj, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size, true);
        try {
            for (int i = 0; i < size; ++i) {
                items.add(classObj.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    T checkOut() throws InterruptedException {
        available.acquire();
        return getItem();
    }

    void checkIn(T item) {
        if (releaseItem(item))
            available.release();
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1) return false;
        if (checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false;
    }

    private synchronized T getItem() {
        for (int i = 0; i < size; ++i) {
            if (!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }
        return null;
    }
}