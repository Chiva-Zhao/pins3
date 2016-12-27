package com.chiva.nio;

import jdk.internal.util.xml.impl.Input;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by air on 2016/12/26.
 */
public class TimeClient_BIO {
    public static void main(String[] args) {
        System.out.println("客户端启动");
        PrintWriter out = null;
        Socket client = null;
        try {
            client = new Socket("127.0.0.1", 8080);
            out = new PrintWriter(client.getOutputStream(), true);
            out.println("show me the time");
            System.out.println("发生服务器命令成功");
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("服务器返回：" + reader.readLine());
            client.close();
            reader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
