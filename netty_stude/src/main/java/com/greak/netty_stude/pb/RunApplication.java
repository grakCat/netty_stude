package com.greak.netty_stude.pb;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
public class RunApplication {

    public static void main(String[] args) {
        NettyServer server = new NettyServer(8000,null);
        NettyClient client = new NettyClient(8000,"127.0.0.1");
        new Thread(server).start();
        new Thread(client).start();
    }
}
