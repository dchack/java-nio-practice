package hopehack.practice.four;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/10/21 1:19 PM
 */
public class MultiThreadServerReactor {

    private ServerSocketChannel serverSocketChannel;
    private AtomicInteger next = new AtomicInteger(0);
    private Selector[] selectors = new Selector[2];
    private SubReactor[] subReactors = null;
    MultiThreadServerReactor() throws IOException {
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(20012);
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);
        SelectionKey sk1 =  serverSocketChannel.register(selectors[0], SelectionKey.OP_ACCEPT);
        sk1.attach(new AcceptorHandler());

        SelectionKey sk2 = serverSocketChannel.register(selectors[1], SelectionKey.OP_ACCEPT);
        sk2.attach(new AcceptorHandler());

        SubReactor subReactor1 = new SubReactor(selectors[0]);
        SubReactor subReactor2 = new SubReactor(selectors[1]);
        SubReactor[] subReactors = new SubReactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        for (int i = 0; i < subReactors.length; i++) {
            new Thread(subReactors[i]);
        }
    }


}
