package project.Trivia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Create database in user's home directory
    private static final String DB_DIR = System.getProperty("user.home") + "/.trivia";
    private static final String URL = "jdbc:sqlite:" + DB_DIR + "/trivia.db";

    static {
        try {
            // Ensure the directory exists
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(DB_DIR));
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}