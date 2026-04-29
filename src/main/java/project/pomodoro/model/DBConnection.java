package project.pomodoro.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static final String DB_DIR = System.getProperty("user.home") + "/.session";
    private static String URL = "JDBC:sqlite:" + System.getProperty("user.home") +  "/.session/session.db";


    static {
        try {
            // Ensures the directory exists
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

