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
                // 等待IO事件
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey sk = it.next();
                    // 分发
                    dispatch(sk);
                }
                selectionKeys.clear();
            }
        } catch (Exception e) {

        }

    }

    private void dispatch(SelectionKey selectionKey) {
        // 分发就是把attach的Handle拿出来执行，注意这里是包括Accept和Read/Write事件key绑定的Handle统一到一个接口下
        Runnable handler = (Runnable) selectionKey.attachment();
        if(handler != null) {
            // 在本线程上执行，这里就把select和handler执行分离开了
            handler.run();
        }
    }
}
