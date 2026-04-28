import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Trivia.dao.DatabaseConnection;
import project.Trivia.dao.DatabaseInitialiser;
import project.Trivia.dao.QuestionsSeeder;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionSeederTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseInitialiser.init();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testSeedInsertsQuestions() throws SQLException {
        QuestionsSeeder.seed();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM questions");
        rs.next();
        assertEquals(20, rs.getInt(1), "Should have 3 seeded questions");

        rs = stmt.executeQuery("SELECT question FROM questions WHERE correct_answer = 'Paris'");
        assertTrue(rs.next(), "Should find the France capital question");
        assertEquals("What is the capital of France?", rs.getString("question"));
    }
}