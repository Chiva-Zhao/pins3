package com.chiva.nio;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by air on 2016/12/26.
 */
public class HandlerClient implements Runnable {
    private Socket socket;

    public HandlerClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            System.out.println("处理客户端请求：");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                String msg = reader.readLine();
                if (msg == null)
                    break;
                System.out.println("接收到客户端请求：" + msg);
                String back_msg = msg.equalsIgnoreCase("show me the time") ? new Date().toString() : "BAD CMD";
                System.out.println("写出客户端：" + back_msg);
                writer.println(back_msg);
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
