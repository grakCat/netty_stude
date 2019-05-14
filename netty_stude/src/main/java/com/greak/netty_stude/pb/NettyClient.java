package com.greak.netty_stude.pb;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyClient implements Runnable{

    private Logger log = LoggerFactory.getLogger(getClass());
    /*端口号*/
    private int port;
    /*用户地址*/
    private String address;
    /*当前开启的Channel*/
    private ChannelFuture cf;

    public NettyClient(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 4096)
                    .option(ChannelOption.SO_KEEPALIVE, true)//保持连接
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new LoggingHandler(LogLevel.INFO))//日志打印
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new ClusterMessageDecoder());
                            sc.pipeline().addLast(new ClusterMessageEncoder());
                            sc.pipeline().addLast("idleStateHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });

            if (address != null) {
                cf = b.connect(address, port).sync();
            }
            log.debug(String.format("Netty_Client address:%s port:%n", this.address, this.port));
            //等待服务器监听端口关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
