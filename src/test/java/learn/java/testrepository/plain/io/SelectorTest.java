package learn.java.testrepository.plain.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@DisplayName("자바 NIO Selector 학습 테스트")
public class SelectorTest {

    private static final int PORT = 8000;
    private static final int TIMEOUT_MILLIS_SECS = 2_000;

    private static final String TEST_JSON = "{\"msg\": \"test\"}";
    private static final Charset CHARSET = Charset.forName("UTF-8");

    Selector selector;
    ServerSocketChannel serverSocketChannel;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @BeforeEach
    void setUp() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
    }

    @AfterEach
    void tearDown() throws IOException {
        selector.close();
        serverSocketChannel.close();
    }

    @Test
    @DisplayName("Selector 로 서버 소켓의 연결 이벤트를 모니터링할 수 있다")
    void selector_accept() throws IOException {
        // given
        connectToServer(PORT);

        // when
        final SelectionKey acceptKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selector.select(TIMEOUT_MILLIS_SECS);

        // then
        final Set<SelectionKey> selectedKeys = selector.selectedKeys();
        assertThat(selectedKeys).containsExactly(acceptKey);
    }

    @Test
    @DisplayName("Selector 에 읽기 이벤트 처리 핸들러를 등록한다")
    void attach_eventHandler() throws IOException {
        // given
        executorService.execute(() -> sendRequestToServer(PORT, TEST_JSON));

        final Handler<Void> acceptHandler = getAcceptHandler();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, acceptHandler);
        selector.select(TIMEOUT_MILLIS_SECS);
        final Set<SelectionKey> acceptSelectionKeys = selector.selectedKeys();
        dispatchSocketEvent(acceptSelectionKeys, selector);

        // then
        selector.select(TIMEOUT_MILLIS_SECS);
        final Set<SelectionKey> readSelectionKeys = selector.selectedKeys();
        final String clientRequest = dispatchSocketEvent(readSelectionKeys, selector);
        assertThat(clientRequest).isEqualTo(TEST_JSON);
    }

    private String dispatchSocketEvent(final Set<SelectionKey> selectedKeys, final Selector selector) throws IOException {
        String result = null;

        for (SelectionKey key : selectedKeys) {
            Handler<String> handler = (Handler<String>) key.attachment();

            if (key.isAcceptable()) {
                log.info("server socket got accept event");
                handler.handle(key, selector);
            }

            if (key.isReadable()) {
                log.info("server socket got read event");
                result = handler.handle(key, selector);
            }
        }

        selectedKeys.clear();

        return result;
    }

    private Handler<Void> getAcceptHandler() {
        return (selectionKey, selector) -> {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ, getReadHandler());
            return null;
        };
    }

    private Handler<String> getReadHandler() {
        return (selectionKey, selector) -> {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.read(buffer);
            buffer.flip();
            return CHARSET.decode(buffer).toString();
        };
    }

    private void connectToServer(final int port) {
        try (final Socket socket = new Socket()) {
            Thread.sleep(1000);
            socket.connect(new InetSocketAddress(port));
            log.info("client socket success to connect with server");
        } catch (IOException | InterruptedException ex) {
            log.error("client socket failed due to error, {}", ex.getMessage());
        }
    }

    private void sendRequestToServer(final int port, final String request) {
        try (final Socket socket = new Socket()) {
            Thread.sleep(1000);
            socket.connect(new InetSocketAddress(port));
            log.info("client socket success to connect with server");

            final OutputStream outputStream = socket.getOutputStream();
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
            writer.print(request);
            log.info("client socket success to send request, {}", request);

            writer.close();
            outputStream.close();
        } catch (IOException | InterruptedException ex){
            log.error("client socket failed due to error, {}", ex.getMessage());
        }
    }

    private interface Handler<T> {

        T handle(SelectionKey selectionKey, Selector selector) throws IOException;
    }
}
