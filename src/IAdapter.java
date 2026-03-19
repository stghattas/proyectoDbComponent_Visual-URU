import java.sql.Connection;
import java.sql.SQLException;

public interface IAdapter {
    Connection createConnection(String host, int port, String dbName, String user, String password) throws SQLException;
}