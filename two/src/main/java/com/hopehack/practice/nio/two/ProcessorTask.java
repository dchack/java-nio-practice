package main.java.com.hopehack.practice.nio.two;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/9/25 10:43 PM
 */
public class ProcessorTask implements Callable {

    private SocketChannel socketChannel;

    public ProcessorTask(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public Object call() throws Exception {



        return null;
    }
}
