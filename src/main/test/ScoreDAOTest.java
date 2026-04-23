

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Trivia.dao.DatabaseConnection;
import project.Trivia.dao.DatabaseInitialiser;
import project.Trivia.dao.ScoreDAO;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreDAOTest {
    private Connection connection;
    private ScoreDAO scoreDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseInitialiser.init();
        scoreDAO = new ScoreDAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testSaveScore() throws SQLException {
        scoreDAO.saveScore("Alice", 5);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT s.score, u.username FROM scores s JOIN users u ON s.user_id = u.id");
        assertTrue(rs.next());
        assertEquals(5, rs.getInt("score"));
        assertEquals("Alice", rs.getString("username"));
    }
}