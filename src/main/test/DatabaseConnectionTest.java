import project.Trivia.dao.DatabaseConnection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    private static final String DB_DIR_PATH = System.getProperty("user.home") + "/.trivia";
    private static final Path DB_DIR = Paths.get(DB_DIR_PATH);

    @BeforeAll
    static void setup() throws Exception {
        // Ensure clean state
        if (Files.exists(DB_DIR)) {
            Files.walk(DB_DIR)
                    .sorted((a, b) -> b.compareTo(a))
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Test
    void testDirectoryCreated() throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn);
        assertTrue(Files.exists(DB_DIR));
        assertTrue(Files.isDirectory(DB_DIR));
        conn.close();
    }

    @Test
    void testConnectionValid() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
        assertTrue(conn.isValid(1));
        conn.close();
        assertTrue(conn.isClosed());
    }

    @Test
    void testDriverLoaded() {
        assertDoesNotThrow(() -> Class.forName("org.sqlite.JDBC"));
    }
}