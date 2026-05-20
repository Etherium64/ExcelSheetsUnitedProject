

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.DatabaseConnection;
import project.model.DatabaseInitialiser;
import project.model.ScoreDAO;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreDAOTest {
    private Connection connection;
    private ScoreDAO scoreDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        scoreDAO = new ScoreDAO();
        scoreDAO.dropTable();
        DatabaseInitialiser.init();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testInsertScore() throws SQLException {
        scoreDAO.insert(1, 5);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT s.scoreValue, u.username FROM scores s JOIN users u ON s.user_id = u.user_id");
        assertTrue(rs.next());
        assertEquals(5, rs.getInt("scoreValue"));
        assertEquals(1, rs.getInt("user_id"));
    }

    @Test
    void testUpdateScore() throws SQLException {
        int previousScore = scoreDAO.getScore(1);
        int nextScore = 3;
        int totalScore = newScore + previousScore;

        assertEquals(5, previousScore);

        scoreDAO.updateScore(1, totalScore);

        assertNotEquals(scoreDAO.getScore(1), previousScore);
        assertNotEquals(scoreDAO.getScore(1), nextScore);
        assertEquals(scoreDAO.getScoe(1), totalScore);
    }

    @
}