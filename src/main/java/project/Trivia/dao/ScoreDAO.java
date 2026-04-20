package project.Trivia.dao;

import java.sql.*;

public class ScoreDAO {
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
