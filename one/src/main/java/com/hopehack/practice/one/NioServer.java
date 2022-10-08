package com.hopehack.practice.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer {

    public static void start() throws IOException {
        // 开启选择器
        Selector selector = Selector.open();
        // 开启server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // server 监听端口绑定
        serverSocketChannel.bind(new InetSocketAddress(12022));
        // channel 注册到选择器上，IO事件为Accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // select 操作 阻塞等待Accept状态就绪
        while (selector.select() > 0) {
            // 获取全部就绪的selectedKeys
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            // 遍历selectedKeys
            while (selectionKeys.hasNext()) {
                SelectionKey selectionKey = selectionKeys.next();
                // 就绪状态为Accept
                if (selectionKey.isAcceptable()) {
                    // 接受一个连接 得到一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置成非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 把SocketChannel 注册到Selector 感兴趣的事件是读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 分配一个新的字节缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    // 从Channel中读取数据到Buffer
                    while ((socketChannel.read(byteBuffer)) > 0) {
                        // 翻转Buffer
                        byteBuffer.flip();
                        // 清理Buffer
                        byteBuffer.clear();
                    }
                    String readStr = new String(byteBuffer.array());
                    System.out.print("" + readStr);
                    socketChannel.close();
                }
                selectionKeys.remove();
            }
//            serverSocketChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        start();
    }

}
