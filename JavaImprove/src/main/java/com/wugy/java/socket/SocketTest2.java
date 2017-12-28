package com.wugy.java.socket;

import com.wugy.java.utils.CloseUtil;
import com.wugy.java.utils.CustomConst;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * wugy 2017-12-28 13:13
 */
public class SocketTest2 {

    @Test
    public void testServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(CustomConst.port);
            Socket socket = serverSocket.accept();
            consoleRead(socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeServerSocket(serverSocket);
        }
    }

    private void consoleRead(Socket socket) throws Exception {
        BufferedReader sysReader = new BufferedReader(
                new InputStreamReader(System.in));
        BufferedReader socketReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        String line = "";
        while (!(line = sysReader.readLine()).equalsIgnoreCase(CustomConst.end)) {
            writer.println(line);
            writer.flush();
            System.out.println("Client line = " + line);
            System.out.println("Server line = " + socketReader.readLine());
        }
        writer.close();
        socketReader.close();
        sysReader.close();
    }

    @Test
    public void testClient() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(CustomConst.ip),
                    CustomConst.port);
            System.out.print("请输入：");
            consoleRead(socket);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeSocket(socket);
        }
    }
}
