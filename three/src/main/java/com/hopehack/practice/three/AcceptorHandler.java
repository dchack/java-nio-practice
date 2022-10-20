package com.hopehack.practice.three;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * handle IO accept event
 *
 * @author hopehack
 * @Date 2022/10/20 1:47 PM
 */
public class AcceptorHandler implements Runnable{

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public AcceptorHandler(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                new IOHandler(selector, socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
