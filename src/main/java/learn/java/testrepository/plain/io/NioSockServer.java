package learn.java.testrepository.plain.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioSockServer {

    private static final int PORT = 8000;
    private static final String RESPONSE = "{ \"response\": \"It worked!\" }";

    public static void main(String[] args) {

        try (ServerSocketChannel serverSockChannel = ServerSocketChannel.open()) {
            // 블로킹 채널, 기본적으로 블로킹 방식으로 동작
            serverSockChannel.configureBlocking(true);
            serverSockChannel.bind(new InetSocketAddress("localhost", 8000));

            log.info("Server is running on port {}", PORT);

            while (true) {
                SocketChannel clientSock = serverSockChannel.accept();
                log.info("Connection {} established", clientSock.getRemoteAddress());

                ByteBuffer requestBuffer = ByteBuffer.allocate(128);
                clientSock.read(requestBuffer);
                requestBuffer.flip();
                Charset charset = Charset.forName("UTF-8");
                String request = charset.decode(requestBuffer).toString();

                log.info("Server accepted message {}", request);

                // 서버 응답 전송
                ByteBuffer responseBuffer = charset.encode(RESPONSE);
                clientSock.write(responseBuffer);

                // 연결 종료
                clientSock.close();
            }
        } catch (IOException e) {
            log.info("Server exception {}", e.getMessage());
        }
    }
}
