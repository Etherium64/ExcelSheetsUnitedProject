import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.DatabaseConnection;
import project.model.DatabaseInitialiser;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing set up of userData.db for Trivia module, incl. creation of Questions table
 */

public class DatabaseInitialiserTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testInitCreatesTables() throws SQLException {
        // Use the test's connection
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseInitialiser.init();

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet tables = meta.getTables(null, null, "questions", null);
        assertTrue(tables.next(), "Questions table should exist");
    }

    @Test
    void testQuestionsTableHasCorrectColumns() throws SQLException {
        DatabaseInitialiser.init();

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet columns = meta.getColumns(connection.getCatalog(), null, "questions", "question");
        assertTrue(columns.next(), "Column 'question' should exist");
    }
}
