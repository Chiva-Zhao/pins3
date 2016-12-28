package com.chiva.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by air on 2016/12/28.
 */
public class TimeClient_NIO {
    public static void main(String[] args) {
        int port = 8080;
        try {
            if (args != null && args.length > 0) {
                port = Integer.valueOf(args[0]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        new Thread(new ClientTask("127.0.0.1", port), "CLIENT-001").start();
    }

    private static class ClientTask implements Runnable {
        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private volatile boolean stop;

        public ClientTask(String host, int port) {
            this.host = host;
            this.port = port;
            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }

        @Override
        public void run() {
            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            while (!stop) {
                try {
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    SelectionKey key = null;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null)
                                    key.channel().close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                SocketChannel sc = (SocketChannel) key.channel();
                if (key.isConnectable()) {
                    if (sc.finishConnect()) {
                        sc.register(selector, SelectionKey.OP_READ);
                        doWrite(sc);
                    } else
                        System.exit(1);
                }
                if (key.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(byteBuffer);
                    if (readBytes > 0) {
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        String msg = new String(bytes, "UTF-8");
                        System.out.println("Now is " + msg);
                        this.stop = true;
                    } else if (readBytes < 0) {
                        key.cancel();
                        sc.close();
                    }
                }
            }
        }

        private void doConnect() throws IOException {
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            } else
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

        private void doWrite(SocketChannel socketChannel) throws IOException {
            String msg = "show me the time";
            byte[] bytes = msg.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            if (!byteBuffer.hasRemaining())
                System.out.println("发生客户端命令成功");
        }
    }
}
