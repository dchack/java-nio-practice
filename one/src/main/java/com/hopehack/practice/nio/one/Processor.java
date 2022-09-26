package main.java.com.hopehack.practice.nio.one;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/9/26 3:56 PM
 */
public class Processor {

    private ByteBuffer readByteBuffer  = ByteBuffer.allocate(1024 * 1024);
    private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);

    private SocketChannel socketChannel;

    public Processor(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void read() throws IOException {
        int bytesRead = socketChannel.read(readByteBuffer);
        while(bytesRead > 0){
            bytesRead = socketChannel.read(readByteBuffer);
        }
    }
}
