package com.greak.netty_stude.length;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
public class RunApplication {

    public static void main(String[] args) {
        Server server = new Server(8000);
        Client client = new Client(8000,"127.0.0.1");
        new Thread(server).start();
        new Thread(client).start();
    }
}
