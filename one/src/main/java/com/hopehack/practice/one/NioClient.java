package com.hopehack.practice.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void start() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(12022);
        // 开启一个Socket Channel 并且连接远程地址
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 连接远程地址
        socketChannel.connect(inetSocketAddress);
        // 等待连接成功
        while (!socketChannel.finishConnect()) {
        }
        // 分配Buffer空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 写入Buffer
        byteBuffer.put("hello world".getBytes());
        // 切换成读模式
        byteBuffer.flip();
        // 把Buffer 写入Socket Channel
        socketChannel.write(byteBuffer);
        // 关闭写连接
        socketChannel.shutdownOutput();
        // 关闭socket Channel
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
