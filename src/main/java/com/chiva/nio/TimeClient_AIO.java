package com.chiva.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by air on 2016/12/31.
 */
public class TimeClient_AIO {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new Thread(new AsynClientHandler("127.0.0.1", port), "AIO-CLIENT").start();
    }

    private static class AsynClientHandler implements Runnable, CompletionHandler<Void, AsynClientHandler> {
        private String host;
        private int port;
        private AsynchronousSocketChannel client;
        private CountDownLatch latch;

        public AsynClientHandler(String host, int port) {
            this.host = host;
            this.port = port;
            try {
                client = AsynchronousSocketChannel.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            latch = new CountDownLatch(1);
            client.connect(new InetSocketAddress(host, port), this, this);
            try {
                latch.await();
                client.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void completed(Void result, AsynClientHandler attachment) {
            byte[] req = "show me the time".getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    if (buffer.hasRemaining())
                        client.write(buffer, buffer, this);
                    else {
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer buffer1) {
                                buffer1.flip();
                                byte[] bytes = new byte[buffer1.remaining()];
                                buffer1.get(bytes);
                                String body = null;
                                try {
                                    body = new String(bytes, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("现在时间为：" + body);
                                latch.countDown();
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                try {
                                    client.close();
                                    latch.countDown();
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
                        client.close();
                        latch.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, AsynClientHandler attachment) {
            exc.printStackTrace();
            try {
                client.close();
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
