package com.greak.netty_stude.length;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
@Slf4j
public class Client implements Runnable{

    /*端口号*/
    private int port;
    /*当前开启的netty*/
    private ChannelFuture cf;
    /*ip地址*/
    private String address;

    public Client(int port,String address){
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>(){

                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置字符串传输数据?
                            ch.pipeline().addLast(new StringDecoder());
                            //定长发送
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                            ch.pipeline().addLast(new ClientHandler());
                            //5秒不传递消息自动断开
                            ch.pipeline().addLast(new ReadTimeoutHandler(5));
                        }
                    });
            //绑定端口，同步等待成功
            cf = bootstrap.connect(address, port).sync();
            //等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
