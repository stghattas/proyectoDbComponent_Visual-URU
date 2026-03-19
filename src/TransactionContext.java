import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class TransactionContext {
    private final Connection connection;
    private final QueryManager queryManager;

    public TransactionContext(Connection connection, QueryManager queryManager) {
        this.connection = connection;
        this.queryManager = queryManager;
    }

    public List<Map<String, Object>> query(String queryName, Object... params) throws Exception {
        String sql = queryManager.getQuery(queryName);
        return DbComponent.executeQueryInternal(connection, sql, params);
    }
}