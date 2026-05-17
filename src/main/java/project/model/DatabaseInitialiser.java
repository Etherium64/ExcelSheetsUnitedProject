package project.model;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility class responsible for initializing the application's database schema and seeding initial data.
 * <p>
 * Provides static methods to create required database tables and populate them with initial data.
 * It is used during application startup to ensure the database is properly set up.
 * </p>
 *
 * @author Ethan B
 */
public class DatabaseInitialiser {
    /**
     * Initializes the database by creating all required tables and seeding questions from QuestionsSeeder.
     * <p>
     * This method should be called once during application startup or else it will duplicate questions.
     * Creates tables if they do not exist, then populates the questions table with predefined data.
     * </p>
     *
     * @throws RuntimeException if a database access error occurs during initialization
     */
    public static void init() {
        createTables();
        project.model.QuestionsSeeder.seed(); // Replace insertSampleQuestions()
    }

    /**
     * Creates the necessary database tables if they do not already exist.
     * <p>
     * This private method executes SQL statements to create three tables:
     * <ul>
     *   <li>{@code users}: Stores user information with a unique username</li>
     *   <li>{@code scores}: Stores user scores with a foreign key reference to users</li>
     *   <li>{@code questions}: Stores trivia questions with multiple choice options and the correct answer</li>
     * </ul>
     * The method uses a try-with-resources statement to ensure proper closure of database connections.
     * Any exceptions during execution are caught and printed to standard error.
     * </p>
     *
     * @throws RuntimeException if a database access error occurs
     */

    private static void createTables() {
        String createScores = "CREATE TABLE IF NOT EXISTS scores (score_id INTEGER PRIMARY KEY, score INTEGER, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(user_id))";
        String createQuestions = "CREATE TABLE IF NOT EXISTS questions (question_id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT NOT NULL UNIQUE, option_a TEXT NOT NULL, option_b TEXT NOT NULL, option_c TEXT NOT NULL, option_d TEXT NOT NULL, correct_answer TEXT NOT NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createScores);
            stmt.execute(createQuestions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}