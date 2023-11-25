package learn.java.testrepository.plain.io;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.common.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@DisplayName("자바 NIO 패키지 학습 테스트")
public class NioTest {

    private final String TEST_JSON = "{ \"data\": \"test success!\" }";

    @Test
    @DisplayName("블로킹 IO - 서버 소켓에 연결하여 응답을 받아온다")
    void io_blocking() throws IOException {
        // given
        Socket sock = new Socket("localhost", 8000);

        // request
        OutputStream clientOutput = sock.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientOutput));
        writer.print("GET " + TEST_JSON + " HTTP/1.0\r\n\r\n");
        writer.flush();

        // when
        InputStream serverInput = sock.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(serverInput));
        StringBuffer ServerResponse = new StringBuffer();

        while (true) {
            String line = reader.readLine();
            if (StringUtils.isBlank(line))
                break;

            ServerResponse.append(line);
        }

        // then
        assertThat(ServerResponse.toString()).isEqualTo("{ \"response\": \"It worked!\" }");
    }

    @Test
    @DisplayName("블로킹 NIO - 서버 소켓에 연결하여 응답을 받아온다")
    void nio_blocking() throws IOException {
        // given
        InetSocketAddress address = new InetSocketAddress("localhost", 8000);
        SocketChannel sockChannel = SocketChannel.open();
        sockChannel.configureBlocking(true);
        sockChannel.connect(address);

        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = charset.encode("GET " + TEST_JSON + " HTTP/1.0\r\n\r\n");
        sockChannel.write(byteBuffer);

        // when
        byteBuffer = ByteBuffer.allocate(128);
        sockChannel.read(byteBuffer);
        byteBuffer.flip();
        String response = charset.decode(byteBuffer).toString();

        // then
        assertThat(response).isEqualTo("{ \"response\": \"It worked!\" }");
    }

    @Test
    @DisplayName("비동기 소켓으로 서버 응답을 받아온다.")
    void asyncChannel() throws IOException, InterruptedException {
        // given
        AsynchronousSocketChannel asyncSockChannel = AsynchronousSocketChannel.open();
        Charset charset = Charset.forName("UTF-8");
        log.info("test started");

        // when
        asyncSockChannel.connect(new InetSocketAddress(8000), null,
            new CompletionHandler<>() {
                @Override
                public void completed(Void result, Object attachment) {
                    log.info("connect to server completed!");
                    ByteBuffer buffer = charset.encode("GET " + TEST_JSON + " HTTP/1.0\r\n\r\n");
                    asyncSockChannel.write(buffer);

                    buffer.flip();
                    asyncSockChannel.read(buffer);
                    String response = charset.decode(buffer).toString();

                    log.info("server send response: {}", response);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    log.error("failed to connect server, {}", exc.getMessage());
                }
            });

        // then
        Thread.sleep(1000);
    }
}
