package com.greak.netty_stude.pb;

import com.greak.netty_stude.module.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    /*所有包含的连接*/
    public static Map<String, Channel> channelMap = new HashMap();
    /*连接秘匙*/
    private String key = "grar";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //证书
//		String auth = "," + key;
//		ctx.writeAndFlush(auth);

        ClusterMessage req = (ClusterMessage) msg;
        if (req.msg.messageType == 666) {
            User message = ProtostuffUtil.deserialize(req.msg.data, User.class);
            System.out.println("id" + req.sessionId + "msg:" + message.toString());
        }

        channelMap.put(req.sessionId, ctx.channel());
        System.out.println("id" + req.sessionId + "msg:" + req.msg);

        //回给客户端
        PFMessage p = new PFMessage();
        p.cmd = 222;
        p.messageType = 666;
        p.data = ProtostuffUtil.serialize(User.builder().name("说好了").age(44));
        ClusterMessage reqs = new ClusterMessage("666", p);
        ctx.writeAndFlush(reqs);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("服务器循环心跳监测发送: " + new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                //超时关闭
                ctx.channel().close();
            } else if (event.state() == IdleState.READER_IDLE) {

            } else if (event.state() == IdleState.ALL_IDLE) {

            }
        }
//		ctx.fireUserEventTriggered(evt);
    }
}
