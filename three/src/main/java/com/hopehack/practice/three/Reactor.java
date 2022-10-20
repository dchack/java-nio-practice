package com.hopehack.practice.three;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 *
 * @author hopehack
 * @Date 2022/10/16 10:23 PM
 */
public class Reactor implements Runnable{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    Reactor () throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(20011));
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptorHandler(serverSocketChannel, selector));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    dispatch(selectionKey);
                }
                selected.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable) selectionKey.attachment();
        if(handler != null) {
            handler.run();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Reactor()).start();
    }
}
