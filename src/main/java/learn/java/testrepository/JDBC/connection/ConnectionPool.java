package learn.java.testrepository.JDBC.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

    Connection getConnection() throws SQLException;

    boolean releaseConnection(Connection connection);

    String getUrl();

    String getUser();

    String getPassword();

    void shutdown() throws SQLException;
}
