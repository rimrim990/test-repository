package learn.java.testrepository.plain.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioSelectorServer {

    private static final int PORT = 8000;

    public static void main(String[] args)  {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress("localhost", PORT));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key : selectedKeys) {

                    // 서버 소켓 - 클라이언트 연결 수락 작업
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    // 서버 소켓 - 읽기 작업
                    if (key.isReadable()) {
                        response(buffer, key);
                    }
                }
            }
        } catch (IOException exception) {
            log.info("Server error occurred {}", exception.getMessage());
        }
    }

    private static void response(ByteBuffer buffer, SelectionKey key) throws IOException {
        // 클라이언트 요청 읽기
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        log.info("client send request: {}", buffer.toString());

        // 클라이언트에 응답 전송
        buffer.flip();
        client.write(buffer);
        buffer.clear();

        key.cancel();
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            log.info("Client Connected");
        }
    }
}
