package com.hopehack.practice.two;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * The server to receive file from a remote client by NIO
 *
 * @author hopehack
 * @Date 2022/10/8 5:17 PM
 */
public class Server {


    public static void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(20023));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        long currentTimeMillis = System.currentTimeMillis();
        String localFile =  "/Users/dongchao/" + currentTimeMillis;
        FileOutputStream fos = new FileOutputStream(localFile);
        FileChannel outFileChannel = fos.getChannel();
        long outLength = 0;
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
                    int length = 0;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(204800);
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        outFileChannel.write(byteBuffer);
                        outLength = outLength + length;
                        byteBuffer.clear();
                        outFileChannel.force(true);
                    }
                    // read 返回-1 客户端关闭连接
                    if (length < 0) {
                        socketChannel.close();
                    }
                }
                iterator.remove();
            }
            System.out.print("file length : " + outLength);
        }
        fos.close();
        outFileChannel.close();
    }

    public static void main(String[] args) throws IOException {
        start();
    }

}
