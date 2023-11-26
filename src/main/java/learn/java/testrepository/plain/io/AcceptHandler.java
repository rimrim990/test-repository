package learn.java.testrepository.plain.io;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptHandler implements Handler {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public AcceptHandler(final Selector selector, final ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void handle() {
        try {
            final SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                new EchoHandler(selector, socketChannel);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
