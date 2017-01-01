package com.chiva.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by air on 2016/12/31.
 */
public class TimeServerHander extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        normal(ctx, (ByteBuf) msg);
        abNormal(ctx, (ByteBuf) msg);
    }

    private void abNormal(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf buf = msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String rsv = new String(bytes, "utf-8").substring(0, bytes.length - System.getProperty("line.separator").length());
        System.out.println("接收到消息：" + rsv + ";计数：" + ++counter);
        String backMsg = rsv.equals("show me the time") ? new Date().toString() : "BAD COMMAND";
        backMsg += System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(backMsg.getBytes());
        ctx.writeAndFlush(resp);
    }

    private void normal(ChannelHandlerContext ctx, ByteBuf msg) throws UnsupportedEncodingException {
        ByteBuf buf = msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String rsv = new String(bytes, "utf-8");
        System.out.println("接收到消息：" + rsv);
        String backMsg = rsv.equals("show me the time") ? new Date().toString() : "BAD COMMAND";
        ByteBuf resp = Unpooled.copiedBuffer(backMsg.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
