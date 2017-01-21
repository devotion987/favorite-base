package com.wugy.java.rpc;

/**
 * devotion on 2017-01-21 19:41
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(String name) {
        System.out.println("hello " + name);
    }
}
