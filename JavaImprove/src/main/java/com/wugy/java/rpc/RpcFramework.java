package com.wugy.java.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * devotion on 2017-01-21 19:07
 */
public class RpcFramework {

    /**
     * 暴露服务
     */
    public static void export(Object service, int port) throws Exception {
        if (null == service)
            throw new IllegalArgumentException("service instance == null");
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port,must between 0 and 65535");
        System.out.println(String.format("export service %s on port %s", service.getClass(), port));

        ServerSocket serverSocket = new ServerSocket(port);
        for (; ; ) {
            Socket socket = serverSocket.accept();

            Thread thread = new Thread(() -> {
                try {
                    threadToAccept(socket, service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.run();
        }
    }

    private static void threadToAccept(Socket socket, Object service) throws Exception {
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        String methodName = input.readUTF();
        Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
        Object[] args = (Object[]) input.readObject();

        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        Object result = method.invoke(service, args);

        output.writeObject(result);

        output.close();
        input.close();
        socket.close();
    }

    /**
     * 引用服务
     */
    @SuppressWarnings("unchecked")
    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
        // 省略校验
        System.out.println(String.format("Get remote service %s from server %s on port %s",
                interfaceClass, host, port));
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    Socket socket = new Socket(host, port);

                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeUTF(method.getName());
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(args);

                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Object result = input.readObject();

                    input.close();
                    output.close();
                    socket.close();
                    return result;
                });
    }
}
