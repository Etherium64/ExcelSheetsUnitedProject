package project.Trivia.dao;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility class responsible for initializing the application's database schema and seeding initial data.
 *
 * @author Ethan B
 */
public class DatabaseInitialiser {

    /**
     * Initializes the database by creating all required tables and seeding questions from QuestionsSeeder.
     */
    public static void init() {
        createTables();
        addAskedColumnIfMissing();
        project.Trivia.dao.QuestionsSeeder.seed();
    }

    /**
     * Creates the necessary database tables if they do not already exist.
     */
    private static void createTables() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE)";
        String createScores = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY, user_id INTEGER, score INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))";

        String createQuestions = """
                CREATE TABLE IF NOT EXISTS questions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    question TEXT NOT NULL UNIQUE,
                    option_a TEXT NOT NULL,
                    option_b TEXT NOT NULL,
                    option_c TEXT NOT NULL,
                    option_d TEXT NOT NULL,
                    correct_answer TEXT NOT NULL,
                    asked INTEGER NOT NULL DEFAULT 0
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsers);
            stmt.execute(createScores);
            stmt.execute(createQuestions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the asked column to older databases that were created before repeat-prevention was added.
     */
    private static void addAskedColumnIfMissing() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("ALTER TABLE questions ADD COLUMN asked INTEGER NOT NULL DEFAULT 0");

        } catch (Exception ignored) {
            // column already exists
        }
    }
}