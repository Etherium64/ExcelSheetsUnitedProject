package project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for managing user scores in the database.
 * <p>
 * This class provides methods to persist user scores by interacting with the
 * {@code users} and {@code scores} tables. It ensures the user exists before saving a score.
 * </p>
 *
 * @author Ethan B
 */
public class ScoreDAO {
    /**
     * Inserts user_id and scoreValue into a new SQL row if this is the first time playing Trivia
     */
    public void insert(int user_id, int scoreValue) {
        String insertScore = "INSERT INTO scores (scoreValue, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(insertScore)) {
                ps.setInt(1, scoreValue);
                ps.setInt(2, user_id);
                ps.execute();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Selects the scoreValue based on the user_id
     * This function is used if this is the first time playing Trivia or not
     */
    public int get(int user_id) {
        String getScoreValue = "SELECT scoreValue FROM scores WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(getScoreValue)) {
                ps.setInt(1, user_id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("scoreValue");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * Updates scoreValue based on the User_id
     * Allows for a cumulative high score
     * Only one scoreVale per user_id
     */
    public void update(int user_id, int scoreValue) {
        String updateScore = "UPDATE scores SET scoreValue = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(updateScore)) {
                ps.setInt(1, scoreValue);
                ps.setInt(2, user_id);
                ps.execute();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void dropTable() {
        String dropTableScores = "DROP TABLE scores";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(dropTableScores)) {
                ps.execute();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
