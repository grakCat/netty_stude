package com.greak.netty_stude.pb;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created on 2018/12/4.
 * 编码器
 *
 * @author grayCat
 * @since 1.0
 */
public class ClusterMessageEncoder extends MessageToMessageEncoder<ClusterMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClusterMessage clusterMessage, List<Object> list) throws Exception {
        list.add(Unpooled.wrappedBuffer(ProtostuffUtil.serialize(clusterMessage)));
    }
}
