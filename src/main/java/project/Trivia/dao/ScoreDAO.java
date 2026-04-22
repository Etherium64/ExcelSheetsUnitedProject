package project.Trivia.dao;

import java.sql.*;

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
     * Saves a score for the specified user.
     * <p>
     * If the user does not already exist in the {@code users} table, they are inserted first.
     * The score is then associated with the user's ID in the {@code scores} table.
     * This is performed within a transaction.
     * </p>
     *
     * @param username the username of the player, must not be null
     * @param score    the score to save, should be impossible to have a non-negative integer
     */
    public void saveScore(String username, int score) {
        String insertUser = "INSERT OR IGNORE INTO users (username) VALUES (?)";
        String getUserId = "SELECT id FROM users WHERE username = ?";
        String insertScore = "INSERT INTO scores (user_id, score) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
                ps.setString(1, username);
                ps.executeUpdate();
            }

            int userId = -1;
            try (PreparedStatement ps = conn.prepareStatement(getUserId)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) userId = rs.getInt("id");
            }

            if (userId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(insertScore)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, score);
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
