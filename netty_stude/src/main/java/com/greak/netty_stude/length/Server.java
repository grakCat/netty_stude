package com.greak.netty_stude.length;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class Server implements Runnable{

    /*端口号*/
    private int port;
    /*当前开启的netty*/
    private ChannelFuture cf;
    /*等待连接最大长度*/
    private static final int SO_BACKLOG = 4096;

    public Server(int port){
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,SO_BACKLOG)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置字符串传输数据?
                            ch.pipeline().addLast(new StringDecoder());
                            //定长发送
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                            ch.pipeline().addLast(new ServerHandler());
                            //5秒不传递消息自动断开
                            ch.pipeline().addLast(new ReadTimeoutHandler(5));
                        }
                    });

            System.out.println("Echo 服务器启动ing");
            //绑定端口，同步等待成功
            cf = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
