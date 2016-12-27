package com.chiva.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by air on 2016/12/26.
 */
public class TimeServer_BIO {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            if (args.length > 0) {
                port = Integer.valueOf(args[0]);
            }
            serverSocket = new ServerSocket(port);
            System.out.println("服务器已启动");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new HandlerClient(socket)).start();
            }
        } catch (Exception e) {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
