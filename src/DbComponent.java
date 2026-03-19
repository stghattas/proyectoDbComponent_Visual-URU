import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DbComponent<T extends IAdapter> {
    private final InternalConnectionPool pool;
    private final QueryManager queryManager;

    public DbComponent(T adapter, String host, int port, String db, String user, String pass, int poolSize, String queryFilePath) {
        this.queryManager = new QueryManager(queryFilePath);
        this.pool = new InternalConnectionPool(adapter, host, port, db, user, pass, poolSize);
    }

    public List<Map<String, Object>> query(String queryName, Object... params) {
        Connection conn = pool.borrowConnection();
        try {
            String sql = queryManager.getQuery(queryName);
            return executeQueryInternal(conn, sql, params);
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando query: " + queryName, e);
        } finally {
            pool.returnConnection(conn);
        }
    }

    public void transaction(Consumer<TransactionContext> txBlock) {
        Connection conn = pool.borrowConnection();
        try {
            conn.setAutoCommit(false);
            TransactionContext context = new TransactionContext(conn, queryManager);
            txBlock.accept(context);
            conn.commit();
        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) {}
            throw new RuntimeException("Transacción fallida, haciendo rollback", e);
        } finally {
            try { conn.setAutoCommit(true); } catch (Exception ex) {}
            pool.returnConnection(conn);
        }
    }

    static List<Map<String, Object>> executeQueryInternal(Connection conn, String sql, Object... params) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            boolean isResultSet = stmt.execute();
            List<Map<String, Object>> results = new ArrayList<>();
            
            if (isResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    ResultSetMetaData md = rs.getMetaData();
                    int columns = md.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columns; ++i) {
                            row.put(md.getColumnName(i), rs.getObject(i));
                        }
                        results.add(row);
                    }
                }
            }
            return results;
        }
    }
}
