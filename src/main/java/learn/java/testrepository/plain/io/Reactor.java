package learn.java.testrepository.plain.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

// 이벤트가 발생하면 핸들러 디스패치
public class Reactor implements Runnable {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    Reactor(final int port) throws IOException {
        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //클라이언트 연결 요청을 처리해줄 핸들러 등록
        selectionKey.attach(new AcceptHandler(selector, serverSocketChannel));
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey selectionKey : selected) {
                    dispatch(selectionKey);
                }

                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void dispatch(final SelectionKey selectionKey) {
        // 등록된 핸들러를 가져와 이벤트를 처리한다
        Handler handler = (Handler) selectionKey.attachment();
        handler.handle();
    }
}
