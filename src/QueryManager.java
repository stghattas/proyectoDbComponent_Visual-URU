import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class QueryManager {
    private final Properties queries;

    public QueryManager(String filePath) {
        queries = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(filePath))) {
            queries.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando el archivo de queries: " + filePath, e);
        }
    }

    public String getQuery(String queryName) {
        String sql = queries.getProperty(queryName);
        if (sql == null) throw new IllegalArgumentException("Query no definida: " + queryName);
        return sql;
    }
} 
