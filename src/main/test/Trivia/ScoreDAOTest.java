import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testing if UserDAO and ScoreDAO work together.
 * Specifically, if a Foreign key connects the users and scores SQLIte tables.
 */

public class ScoreDAOTest {
    private Connection connection;
    private ScoreDAO scoreDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseConnection.setUrlForTest("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");
        DatabaseInitialiser.init();
        scoreDAO = new ScoreDAO();

        userDAO = new UserDAO();
        userDAO.dropTable();
        userDAO.createTable();
        userDAO.insert(new User("", "", null, false));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
        userDAO.close();
    }

    @Test
    void testSaveScore() throws SQLException {
        scoreDAO.insert(1, 5);

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT s.scoreValue, u.user_id FROM scores s JOIN users u ON s.user_id = u.user_id");
        assertTrue(rs.next());
        assertEquals(5, rs.getInt("scoreValue"));
        assertEquals(1, rs.getInt("user_id"));
    }
}