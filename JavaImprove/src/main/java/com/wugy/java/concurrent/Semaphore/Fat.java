package com.wugy.java.concurrent.Semaphore;

/**
 * wugy on 2017/10/19 11:28
 */
public class Fat {

    private static int count = 0;
    private final int id = count++;

    @Override
    public String toString() {
        return "Fat{" + "id=" + id + '}';
    }

    public void operation() {
        System.out.println(this);
    }
}
