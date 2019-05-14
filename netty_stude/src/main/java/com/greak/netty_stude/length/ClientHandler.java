package com.greak.netty_stude.length;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        System.out.println("Client: " + str);

        String send = str + ",12345678910";
        if(send.length() >= 100){
            return;
        }
        ctx.writeAndFlush(Unpooled.copiedBuffer(send.getBytes()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client Active");
        ctx.writeAndFlush(Unpooled.copiedBuffer("初始消息12345678".getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
