package com.chiva.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by air on 2016/12/29.
 */
public class TimeServer_AIO {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        AsynServerHandler serverHandler = new AsynServerHandler(port);
        new Thread(serverHandler, "AIO-Server").start();

    }

    private static class AsynServerHandler implements Runnable {
        private int port;
        CountDownLatch latch;
        AsynchronousServerSocketChannel serverSocketChannel;

        public AsynServerHandler(int port) {
            this.port = port;
            try {
                serverSocketChannel = AsynchronousServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress(port));
                System.out.println("AIO server start at port:" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            latch = new CountDownLatch(1);
            doAccept();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doAccept() {
            serverSocketChannel.accept(this, new AcceptCompletionHandler());
        }

        private class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynServerHandler> {
            @Override
            public void completed(AsynchronousSocketChannel result, AsynServerHandler attachment) {
                attachment.serverSocketChannel.accept(attachment, this);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                result.read(buffer, buffer, new ReadCompletionHandler(result));
            }

            @Override
            public void failed(Throwable exc, AsynServerHandler attachment) {
                attachment.latch.countDown();
            }
        }

        private class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
            private AsynchronousSocketChannel channel;

            public ReadCompletionHandler(AsynchronousSocketChannel channel) {
                if (this.channel == null)
                    this.channel = channel;
            }

            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                byte[] body = new byte[attachment.remaining()];
                attachment.get(body);
                try {
                    String req = new String(body, "UTF-8");
                    System.out.println("查询语句：" + req);
                    String currentTime = req.equalsIgnoreCase("show me the time") ? new Date().toString() : "BAD ORDER";
                    doWrite(currentTime);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            private void doWrite(String currentTime) {
                if (currentTime != null && currentTime.trim().length() > 0) {
                    byte[] bytes = currentTime.getBytes();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                    writeBuffer.put(bytes);
                    writeBuffer.flip();
                    channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            if (buffer.hasRemaining())
                                channel.write(buffer, buffer, this);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
