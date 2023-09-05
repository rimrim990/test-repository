package learn.java.testrepository.JDBC.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicConnectionPool implements ConnectionPool {

    private static int CONNECTION_POOL_SIZE = 10;
    private static int MAX_TIMEOUT = 1;

    private final String url;
    private final String user;
    private final String password;
    private final List<Connection> connectionPool;

    private List<Connection> usedConnection = new ArrayList<>();

    public static ConnectionPool from(final String url, final String user, final String password) throws SQLException {
        // 커넥션 풀 크기만큼 DB 커넥션을 미리 생성해놓는다
        List<Connection> connectionPool = new ArrayList<>(CONNECTION_POOL_SIZE);
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            connectionPool.add(createConnection(url, user, password));
        }
        return new BasicConnectionPool(url, user, password, connectionPool);
    }

    public static Connection createConnection(final String url, final String user, final String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        // 사용 가능한 커넥션이 없으면 예외를 던진다
        if (connectionPool.isEmpty()) {
            throw new RuntimeException("No available connections");
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        // 커넥션이 아직 유효한지 확인한다
        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection(url, user, password);
        }

        // 사용중인 커넥션 리스트에 추가한다
        usedConnection.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        // connection 이 커넥션 풀에서 관리하는 커넥션인지 확인한다
        if (usedConnection.remove(connection)) {
            connectionPool.add(connection);
            return true;
        }
        return false;
    }

    public int getSize() {
        return connectionPool.size() + usedConnection.size();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void shutdown() throws SQLException {
        usedConnection.forEach(this::releaseConnection);

        for (Connection connection : connectionPool) {
            connection.close();
        }
    }
}
