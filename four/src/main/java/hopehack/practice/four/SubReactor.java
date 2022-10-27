package hopehack.practice.four;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/10/21 5:00 PM
 */
public class SubReactor implements Runnable{

    private Selector selector;

    public SubReactor(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (! Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selectionKeys.clear();
            }
        } catch (Exception e) {

        }

    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable handler = (Runnable) selectionKey.attachment();
        if(handler != null) {
            handler.run();
        }
    }
}
