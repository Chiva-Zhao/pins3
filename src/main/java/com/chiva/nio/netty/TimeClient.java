package com.chiva.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by air on 2017/1/1.
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new TimeClient().connect("127.0.0.1", port);
    }

    private void connect(String host, int port) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(worker).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());
                    ch.pipeline().addLast(new TimeClientHandler_AN());
                }
            });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }

    }
}
