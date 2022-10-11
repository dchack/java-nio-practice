package com.hopehack.practice.two;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * send a file to remote server
 *
 * @author dongchao
 * @Date 2022/10/8 5:17 PM
 */
public class Server {


    public static void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(20023));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

        long currentTimeMillis = System.currentTimeMillis();
        String localFile =  "/Users/dongchao/"+currentTimeMillis;

        while(selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.read(byteBuffer);

                    int remaining = byteBuffer.remaining();
                    int capacity = byteBuffer.capacity();
                    byte[] copyBytes = new byte[capacity - remaining];
                    byteBuffer.get(copyBytes, 0, capacity - remaining);

                    FileOutputStream outputStream = new FileOutputStream(localFile);
                    outputStream.write(copyBytes);
                    outputStream.close();
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        start();
    }

}
