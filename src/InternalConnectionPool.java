import java.sql.Connection;
import java.util.LinkedList;
import java.util.Queue;

public class InternalConnectionPool {
    private final Queue<Connection> pool;

    public InternalConnectionPool(IAdapter adapter, String host, int port, String db, String user, String pass, int poolSize) {
        pool = new LinkedList<>();
        try {
            for (int i = 0; i < poolSize; i++) {
                pool.add(adapter.createConnection(host, port, db, user, pass));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando el pool de conexiones", e);
        }
    }

    public Connection borrowConnection() {
        if (pool.isEmpty()) {
            throw new RuntimeException("No hay conexiones disponibles en el pool en este momento.");
        }
        return pool.poll();
    }

    public void returnConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }
}