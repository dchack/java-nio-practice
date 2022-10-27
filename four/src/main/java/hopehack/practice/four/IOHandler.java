package hopehack.practice.four;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/10/27 10:33 PM
 */
public class IOHandler implements Runnable{

    SocketChannel socketChannel;
    SelectionKey selectionKey;
    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
    int state = 0;

    public IOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        this.selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try{
            if (state == 0) {
                int length =  0;
                while ((length = socketChannel.read(byteBuffer)) > 0) {

                }
                byteBuffer.flip();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                state = 1;
            } else if(state == 1) {
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                selectionKey.interestOps(SelectionKey.OP_READ);
                state = 0;
            }
        } catch (Exception e) {

        }
    }
}
