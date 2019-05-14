package com.greak.netty_stude.length;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String)msg;
        System.out.println("Server: " + str);

        String send = str + "， 123456789 ";
        ctx.writeAndFlush(Unpooled.copiedBuffer(send.getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
