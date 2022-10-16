package com.hopehack.practice.three;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Stack;

/**
 * TODO
 *
 * @author hopehack
 * @Date 2022/10/16 10:39 PM
 */
public class IOHandler implements Runnable{
    SocketChannel socketChannel;
    SelectionKey selectionKey;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
    private static int RECEIVING = 0;
    private static int SENDING = 1;
    private int STATE = RECEIVING;

    public IOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if(STATE == RECEIVING) {
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                selectionKey.interestOps(SelectionKey.OP_READ);
                STATE = RECEIVING;
            }
            else if (STATE == SENDING) {
                int length = 0;
                while ((length = socketChannel.read(byteBuffer)) > 0) {

                }
                byteBuffer.flip();
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                STATE = SENDING;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
