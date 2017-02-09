package com.wugy.java.proxy;

/**
 * devotion on 2017-02-09 8:33.
 */
public class CountServiceImpl implements CountService {

    private int count = 0;

    @Override
    public int count() {
        return count++;
    }
}
