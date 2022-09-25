package com.hopehack.practice.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author hopehack
 * @Date 2022/9/25 10:29 PM
 */
public class Acceptor {

    public void run() throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
//        socketChannel.configureBlocking(false);
        // 监听端口
        socketChannel.bind(new InetSocketAddress(1));

        while (true) {
            SocketChannel accept = socketChannel.accept();



        }





    }

}
