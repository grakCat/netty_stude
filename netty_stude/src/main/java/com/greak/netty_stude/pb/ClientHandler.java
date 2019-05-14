package com.greak.netty_stude.pb;

import com.greak.netty_stude.module.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        PFMessage p = new PFMessage();
        p.cmd = 111;
        p.messageType = 666;
        p.data = ProtostuffUtil.serialize(User.builder().name("黄晓明").age(44));
        ClusterMessage req = new ClusterMessage("55555", p);
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClusterMessage req = (ClusterMessage) msg;
        System.out.println("id" + req.sessionId + "msg:" + req.msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("客户端循环心跳监测发送: " + new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {

            } else if (event.state() == IdleState.READER_IDLE) {

            } else if (event.state() == IdleState.ALL_IDLE) {

            }
        }
//		ctx.fireUserEventTriggered(evt);
    }

}
