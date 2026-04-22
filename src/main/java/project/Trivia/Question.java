package project.Trivia;

/**
 * Represents a trivia question with four multiple-choice options and the correct answer.
 * <p>
 * This class is a simple data model (POJO) used to encapsulate question content
 * and answer choices. It is typically populated from the database via DAOs
 * and used by the application to present questions to users.
 * </p>
 *
 * @author Ethan B
 */

public class Question {

    String question = null;
    //Answer choices
    String answerOne = null;
    String answerTwo = null;
    String answerThree = null;
    String answerFour = null;
    //Correct answer
    String correctAnswer = null;

    /**
     * Generates the trivia question alongside the answer options and the correct answer, reading from QuestionsSeeder.java
     * @param question       the text of the trivia question, must not be null
     * @param answerOne      the first answer option (A), must not be null
     * @param answerTwo      the second answer option (B), must not be null
     * @param answerThree    the third answer option (C), must not be null
     * @param answerFour     the fourth answer option (D), must not be null
     * @param correctAnswer  the correct answer, must match one of the four options
     */

    public Question(String question, String answerOne, String answerTwo,
                    String answerThree, String answerFour, String correctAnswer) {

        this.question = question;
        //Answer choices
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        //Correct answer
        this.correctAnswer = correctAnswer;
    }


}