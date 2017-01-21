package com.wugy.java.rpc;

import org.junit.Test;

/**
 * devotion on 2017-01-21 19:43
 */
public class RpcProvider {

    @Test
    public void testRpcProvider () {
        HelloService helloService = new HelloServiceImpl();
        try {
            RpcFramework.export(helloService, 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
