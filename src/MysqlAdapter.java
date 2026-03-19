import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlAdapter implements IAdapter {
    @Override
    public Connection createConnection(String host, int port, String dbName, String user, String password) throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
        return DriverManager.getConnection(url, user, password);
    }
}