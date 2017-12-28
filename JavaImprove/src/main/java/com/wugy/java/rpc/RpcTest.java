package com.wugy.java.rpc;

import org.junit.Test;

/**
 * wugy 2017-12-28 13:19
 */
public class RpcTest {

    @Test
    public void testRpcProvider () {
        HelloService helloService = new HelloServiceImpl();
        try {
            RpcFramework.export(helloService, 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
