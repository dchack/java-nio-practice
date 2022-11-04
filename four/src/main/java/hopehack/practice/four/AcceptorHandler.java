package hopehack.practice.four;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/10/21 1:26 PM
 */
public class AcceptorHandler implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    AcceptorHandler(Selector selector, ServerSocketChannel serverSocketChannel){
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                new IOHandler(selector, socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
