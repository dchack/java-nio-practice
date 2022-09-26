package main.java.com.hopehack.practice.nio.one;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/9/25 10:24 PM
 */
public class Server {


    public void start() throws IOException {
        int port = 7001;


        ServerSocketChannel socketChannel = ServerSocketChannel.open();
//        socketChannel.configureBlocking(false);
        // 监听端口
        socketChannel.bind(new InetSocketAddress(12022));

        Selector readSelector = Selector.open();


        while (true) {
            SocketChannel acceptSocketChannel = socketChannel.accept();

            SelectionKey readSelectKey = acceptSocketChannel.register(readSelector, SelectionKey.OP_READ);
            readSelectKey.attach(new Processor(acceptSocketChannel));

            int readReady = readSelector.selectNow();

            if (readReady > 0) {
                Set<SelectionKey> selectedKeys = readSelector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while(keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    Processor processor = (Processor) key.attachment();
                    processor.read();
                    keyIterator.remove();
                }
            }

        }

    }

}
