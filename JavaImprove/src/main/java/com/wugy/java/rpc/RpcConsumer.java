package com.wugy.java.rpc;

import org.junit.Test;

/**
 * devotion on 2017-01-21 19:44
 */
public class RpcConsumer {

    @Test
    public void testRpcConsumer() {
        try {
            HelloService helloService = RpcFramework.refer(HelloService.class, "127.0.0.1", 1234);

            for (int i = 0; i < 1000; i++) {
                helloService.sayHello("world " + i);

                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
