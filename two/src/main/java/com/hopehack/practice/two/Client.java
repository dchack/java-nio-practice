package com.hopehack.practice.two;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/10/11 10:50 PM
 */
public class Client {

    public static void start() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(20023);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(inetSocketAddress);
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        FileChannel fileChannel =  new FileInputStream("/Users/dongchao/in").getChannel();
        if (socketChannel.finishConnect()) {
            while (fileChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        }
        fileChannel.close();
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
