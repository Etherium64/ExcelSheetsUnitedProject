package project.Trivia.dao;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitialiser {
    public static void init() {
        createTables();
        project.Trivia.dao.QuestionsSeeder.seed(); // Replace insertSampleQuestions()
    }

    private static void createTables() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE)";
        String createScores = "CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY, user_id INTEGER, score INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))";
        String createQuestions = "CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT NOT NULL, option_a TEXT NOT NULL, option_b TEXT NOT NULL, option_c TEXT NOT NULL, option_d TEXT NOT NULL, correct_answer TEXT NOT NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createScores);
            stmt.execute(createQuestions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}