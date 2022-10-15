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
        // 开启Selector
        Selector selector = Selector.open();
        // 开启一个Server Socket Channel 用于监听连接Accept事件
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 监听端口
        serverSocketChannel.bind(new InetSocketAddress(20023));
        // 设置Selector 多路复用监听Accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 产生一个文件名
        String localFile = getFileName();
        // 获取一个File Channel
        FileOutputStream fos = new FileOutputStream(localFile);
        FileChannel outFileChannel = fos.getChannel();
        // 记录文件字节量
        long outLength = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(204800);

        // 阻塞选择准备好进行I/O操作的键
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
                    int length;
                    // 对于Socket Channel来说是从Channel上读取数据，写入到ByteBuffer
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        // 切换成读模式
                        byteBuffer.flip();
                        // 对于File Channel来说是读取ByteBuffer数据，写入到Channel
                        outFileChannel.write(byteBuffer);
                        outLength = outLength + length;
                        // 切换成写模式
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

    private static String getFileName() {
        long currentTimeMillis = System.currentTimeMillis();
        return "/Users/dongchao/" + currentTimeMillis;
    }

    public static void main(String[] args) throws IOException {
        start();
    }

}
