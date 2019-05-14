package com.greak.netty_stude.separator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        String request = buf.toString(CharsetUtil.UTF_8);
        System.out.println("Client: " + request);

        String send = request + ",20@@@@";
        if(send.length() >= 20){
            return;
        }
        ctx.writeAndFlush(Unpooled.copiedBuffer(send.getBytes()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client Active");
        ctx.writeAndFlush(Unpooled.copiedBuffer("消息@@@@12155@@@@",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
