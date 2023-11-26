package learn.java.testrepository.plain.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoHandler implements Handler {

    private static final int READING = 0, SENDING = 1;

    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;
    private final ByteBuffer buffer = ByteBuffer.allocate(256);
    private int state = READING;

    public EchoHandler(final Selector selector, final SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);

        selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
        selector.wakeup();
    }

    @Override
    public void handle() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void read() throws IOException {
        try {
            int readCount = socketChannel.read(buffer);
            if (readCount > 0) {
                buffer.flip();
            }
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            state = SENDING;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            selectionKey.cancel();
            socketChannel.close();
        }
    }

    private void send() throws IOException {
        try {
            socketChannel.write(buffer);
            buffer.clear();
            selectionKey.interestOps(SelectionKey.OP_READ);
            state = READING;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            selectionKey.cancel();
            socketChannel.close();
        }
    }
}
