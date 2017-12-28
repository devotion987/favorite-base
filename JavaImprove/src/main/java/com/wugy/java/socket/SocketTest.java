package com.wugy.java.socket;

import com.wugy.java.utils.CloseUtil;
import com.wugy.java.utils.CustomConst;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * wugy 2017-12-28 13:08
 */
public class SocketTest {

    @Test
    public void testServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(CustomConst.port);
            System.out.println("服务端正在接收。。。");
            for (; ; ) {
                Socket socket = serverSocket.accept();
                new Thread(new ReadThread(socket)).start();
                new Thread(new WriteThread(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeServerSocket(serverSocket);
        }
    }

    @Test
    public void testClient() {
        Socket socket = null;
        try {
            socket = new Socket(CustomConst.ip, CustomConst.port);
            new Thread(new ReadThread(socket)).start();
            new Thread(new WriteThread(socket)).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeSocket(socket);
        }
    }

    private static final class ReadThread implements Runnable {

        private Socket socket;

        ReadThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            DataInputStream dataInputStream = null;
            try {
                for (; ; ) {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    String receive = dataInputStream.readUTF();
                    System.out.println("读取的数据：" + receive);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CloseUtil.closeInputStream(dataInputStream);
            }
        }

    }

    private static final class WriteThread implements Runnable {

        private Socket socket;

        WriteThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            DataOutputStream outputStream = null;
            BufferedReader reader = null;
            try {
                for (; ; ) {
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.print("请输入：");
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    String send = reader.readLine();
                    outputStream.writeUTF(send);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CloseUtil.closeOutputStream(outputStream);
                CloseUtil.closeReader(reader);
            }
        }

    }

}
