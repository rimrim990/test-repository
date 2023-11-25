package learn.java.testrepository.plain.io;

import io.micrometer.common.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SockServer {

    private static final int PORT = 8000;
    private static final String RESPONSE = "{ \"response\": \"It worked!\" }";

    public static void main(String[] args) {

        try (ServerSocket sock = new ServerSocket(PORT)) {
            log.info("Server is running on port {}", PORT);

            while (true) {
                Socket clientSock = sock.accept();
                log.info("Connection {} established", clientSock.getInetAddress());

                InputStream clientInput = clientSock.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientInput));
                StringBuffer clientRequest = new StringBuffer();

                // 클라이언트 요청 수신
                while (true) {
                    String line = reader.readLine();
                    if (StringUtils.isBlank(line))
                        break;

                    clientRequest.append(line);
                    clientRequest.append(System.lineSeparator());
                }

                log.info("Server accepted message {}", clientRequest);

                // 서버 응답 전송
                OutputStream serverOutput = clientSock.getOutputStream();
                PrintWriter writer = new PrintWriter(serverOutput);
                writer.print(RESPONSE);
                writer.flush();

                // 연결 종료
                clientSock.close();
            }
        } catch (IOException e) {
            log.info("Server exception {}", e.getMessage());
        }
    }
}
