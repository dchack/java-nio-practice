package main.java.com.hopehack.practice.nio.one.server;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/9/26 4:15 PM
 */
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
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while ((socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        byteBuffer.clear();
                    }

                    socketChannel.close();
                }
                selectionKeys.remove();
            }
            serverSocketChannel.close();
        }
    }


    public static void main(String[] args) throws IOException {
        start();
    }

}
