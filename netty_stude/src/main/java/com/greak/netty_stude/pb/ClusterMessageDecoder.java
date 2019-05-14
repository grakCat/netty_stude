package com.greak.netty_stude.pb;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created on 2018/12/4.
 * 解码器
 *
 * @author grayCat
 * @since 1.0
 */
public class ClusterMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(0, array);
        list.add(ProtostuffUtil.deserialize(array, ClusterMessage.class));
    }
}
