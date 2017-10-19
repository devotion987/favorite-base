package com.wugy.java.concurrent.Semaphore;

import java.util.concurrent.TimeUnit;

/**
 * wugy on 2017/10/19 11:32
 */
public class CheckOutTask<T> implements Runnable {

    private static int count = 0;
    private final int id = count++;
    private Pool<T> pool;

    public CheckOutTask(Pool<T> pool) {
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
