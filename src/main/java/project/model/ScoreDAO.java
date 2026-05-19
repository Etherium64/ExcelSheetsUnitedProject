package project.model;

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
     * @param user_id    the userId of the player, must not be null
     * @param scoreValue the score to save, should be impossible to have a non-negative integer
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

    public int get (int user_id) {
        String getScore = "SELECT scoreValue FROM scores WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection())
        {
            assert conn != null;
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(getScore)) {
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


    public void update (int user_id, int scoreValue) {
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
}
