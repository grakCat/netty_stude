package com.greak.netty_stude.pb;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyServer implements Runnable{

    private Logger log = LoggerFactory.getLogger(getClass());
    /*端口号*/
    private int port;
    /*用户地址*/
    private String address;
    /*当前开启的Channel*/
    private ChannelFuture cf;

    private static final int SO_BACKLOG = 4096;
    private static final int HANDER_SIZE = 4;

    public NettyServer(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void run() {
        //1 创建2个线程，一个是负责接收客户端的连接。一个是负责进行数据传输的
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            //2 创建服务器辅助类
            ServerBootstrap b = new ServerBootstrap();
            b.group(pGroup, cGroup)
                    .channel(NioServerSocketChannel.class)
                    //建议设置到4K（等待连接的最大数量）
                    .option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
                    //保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))//日志打印
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
//							sc.pipeline().addLast(new LengthFieldPrepender(HANDER_SIZE));//将该长度添加到ByteBuf的缓冲区头中
                            sc.pipeline().addLast(new ClusterMessageDecoder());
                            sc.pipeline().addLast(new ClusterMessageEncoder());
                            //如果readerIdleTime时间内channelRead没有处理消息，会调用一次userEventTriggered
                            //如果writerIdleTime时间内没有发送消息（writeAndFlush）就会调用userEventTriggered
                            //allIdleTime包括以上2种
                            sc.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 20, 30, TimeUnit.SECONDS));
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });

            if (address != null) {
                cf = b.bind(address, port).sync();
            } else {
                cf = b.bind(port).sync();
            }

            log.debug(String.format("Netty_Server address:%s port:%s", this.address, this.port));
            //等待服务器监听端口关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
        }
    }


}
