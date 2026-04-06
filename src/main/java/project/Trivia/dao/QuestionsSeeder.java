package project.Trivia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class QuestionsSeeder {
    public static void seed() {
        String sql = "INSERT OR IGNORE INTO questions (question, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Question 1
            ps.setString(1, "What is the capital of France?");
            ps.setString(2, "London");
            ps.setString(3, "Berlin");
            ps.setString(4, "Paris");
            ps.setString(5, "Madrid");
            ps.setString(6, "Paris");
            ps.executeUpdate();

            // Question 2
            ps.setString(1, "Which planet is known as the Red Planet?");
            ps.setString(2, "Venus");
            ps.setString(3, "Mars");
            ps.setString(4, "Jupiter");
            ps.setString(5, "Saturn");
            ps.setString(6, "Mars");
            ps.executeUpdate();

            // Question 3
            ps.setString(1, "What is the largest mammal?");
            ps.setString(2, "Elephant");
            ps.setString(3, "Blue Whale");
            ps.setString(4, "Giraffe");
            ps.setString(5, "Hippopotamus");
            ps.setString(6, "Blue Whale");
            ps.executeUpdate();

            // Question 4
            ps.setString(1, "Who painted the Mona Lisa?");
            ps.setString(2, "Van Gogh");
            ps.setString(3, "Picasso");
            ps.setString(4, "Da Vinci");
            ps.setString(5, "Rembrandt");
            ps.setString(6, "Da Vinci");
            ps.executeUpdate();

            // Question 5
            ps.setString(1, "What is the chemical symbol for water?");
            ps.setString(2, "O2");
            ps.setString(3, "CO2");
            ps.setString(4, "H2O");
            ps.setString(5, "NaCl");
            ps.setString(6, "H2O");
            ps.executeUpdate();

            // Question 6
            ps.setString(1, "Which country is home to the kangaroo?");
            ps.setString(2, "New Zealand");
            ps.setString(3, "South Africa");
            ps.setString(4, "Australia");
            ps.setString(5, "Brazil");
            ps.setString(6, "Australia");
            ps.executeUpdate();

            // Question 7
            ps.setString(1, "What is the tallest mountain in the world?");
            ps.setString(2, "K2");
            ps.setString(3, "Mount Kilimanjaro");
            ps.setString(4, "Mount Fuji");
            ps.setString(5, "Mount Everest");
            ps.setString(6, "Mount Everest");
            ps.executeUpdate();

            // Question 8
            ps.setString(1, "Which element has the atomic number 1?");
            ps.setString(2, "Helium");
            ps.setString(3, "Oxygen");
            ps.setString(4, "Hydrogen");
            ps.setString(5, "Carbon");
            ps.setString(6, "Hydrogen");
            ps.executeUpdate();

            // Question 9
            ps.setString(1, "Who wrote 'Romeo and Juliet'?");
            ps.setString(2, "Charles Dickens");
            ps.setString(3, "Jane Austen");
            ps.setString(4, "William Shakespeare");
            ps.setString(5, "Mark Twain");
            ps.setString(6, "William Shakespeare");
            ps.executeUpdate();

            // Question 10
            ps.setString(1, "What is the largest ocean on Earth?");
            ps.setString(2, "Atlantic Ocean");
            ps.setString(3, "Indian Ocean");
            ps.setString(4, "Arctic Ocean");
            ps.setString(5, "Pacific Ocean");
            ps.setString(6, "Pacific Ocean");
            ps.executeUpdate();

            // Question 11
            ps.setString(1, "Which gas do plants absorb from the atmosphere?");
            ps.setString(2, "Oxygen");
            ps.setString(3, "Nitrogen");
            ps.setString(4, "Carbon Dioxide");
            ps.setString(5, "Hydrogen");
            ps.setString(6, "Carbon Dioxide");
            ps.executeUpdate();

            // Question 12
            ps.setString(1, "What is the closest star to Earth?");
            ps.setString(2, "Proxima Centauri");
            ps.setString(3, "Alpha Centauri");
            ps.setString(4, "Sun");
            ps.setString(5, "Sirius");
            ps.setString(6, "Sun");
            ps.executeUpdate();

            // Question 13
            ps.setString(1, "Which planet has the most moons?");
            ps.setString(2, "Jupiter");
            ps.setString(3, "Saturn");
            ps.setString(4, "Uranus");
            ps.setString(5, "Neptune");
            ps.setString(6, "Saturn");
            ps.executeUpdate();

            // Question 14
            ps.setString(1, "What is the main ingredient in guacamole?");
            ps.setString(2, "Tomato");
            ps.setString(3, "Onion");
            ps.setString(4, "Avocado");
            ps.setString(5, "Pepper");
            ps.setString(6, "Avocado");
            ps.executeUpdate();

            // Question 15
            ps.setString(1, "Which country invented tea?");
            ps.setString(2, "India");
            ps.setString(3, "England");
            ps.setString(4, "Japan");
            ps.setString(5, "China");
            ps.setString(6, "China");
            ps.executeUpdate();

            // Question 16
            ps.setString(1, "What is the smallest country in the world?");
            ps.setString(2, "Monaco");
            ps.setString(3, "Maldives");
            ps.setString(4, "San Marino");
            ps.setString(5, "Vatican City");
            ps.setString(6, "Vatican City");
            ps.executeUpdate();

            // Question 17
            ps.setString(1, "Which animal is known as the 'Ship of the Desert'?");
            ps.setString(2, "Horse");
            ps.setString(3, "Camel");
            ps.setString(4, "Elephant");
            ps.setString(5, "Donkey");
            ps.setString(6, "Camel");
            ps.executeUpdate();

            // Question 18
            ps.setString(1, "What is the hardest natural substance on Earth?");
            ps.setString(2, "Gold");
            ps.setString(3, "Iron");
            ps.setString(4, "Diamond");
            ps.setString(5, "Platinum");
            ps.setString(6, "Diamond");
            ps.executeUpdate();

            // Question 19
            ps.setString(1, "Which language has the most native speakers?");
            ps.setString(2, "English");
            ps.setString(3, "Hindi");
            ps.setString(4, "Spanish");
            ps.setString(5, "Mandarin");
            ps.setString(6, "Mandarin");
            ps.executeUpdate();

            // Question 20
            ps.setString(1, "In which year did World War II end?");
            ps.setString(2, "1944");
            ps.setString(3, "1945");
            ps.setString(4, "1946");
            ps.setString(5, "1943");
            ps.setString(6, "1945");
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}