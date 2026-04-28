package project.Trivia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Utility class for managing SQLite database connections.
 * <p>
 * This class provides a static database connection to an SQLite database stored
 * in the user's home directory under {@code .trivia/trivia.db}. It ensures the
 * directory exists on startup and loads the SQLite JDBC driver.
 * </p>
 * <p>
 * Thread-safe for concurrent use. Designed to be used by calling {@link #getConnection()}.
 * A test hook {@link #setUrlForTest(String)} is provided for unit testing.
 * </p>
 *
 * @author Ethan B
 */

public class DatabaseConnection {
    // Create database in user's home directory
    private static final String DB_DIR = System.getProperty("user.home") + "/.trivia";
    private static String URL = "JDBC:sqlite:" + System.getProperty("user.home") + "/.trivia/trivia.db";

    /**
     * Initializer that ensures the database directory exists and loads the SQLite JDBC driver.
     * <p>
     * If the {@code .trivia} directory does not exist, it is created.
     * The {@code org.sqlite.JDBC} driver is loaded via {@code Class.forName}.
     * </p>
     *
     * @throws RuntimeException if the driver fails to load or directory cannot be created
     */
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

    /**
     * Retrieves a connection to the SQLite database.
     * <p>
     * Uses the JDBC DriverManager to establish a connection to the database
     * at the predefined URL. This method may return {@code null} if the
     * connection fails, which should be handled by the caller.
     * </p>
     *
     * @return a {@link Connection} object to the database, or {@code null} on failure
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sets a custom database URL for testing purposes.
     * <p>
     * Intended for use in unit or integration tests to redirect database operations to an
     * in-memory or temporary database.
     * Shouldn't be used in production code.
     * </p>
     *
     * @param testUrl the new database URL to use, e.g., "jdbc:sqlite::memory:"
     */
    public static void setUrlForTest(String testUrl) {
        URL = testUrl;
    }
}