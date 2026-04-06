package project.Trivia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Trivia {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //scanner to read txt file input
        Scanner scan = new Scanner(new File("questions.txt")); //questions.txt must stay in the main project folder.
        ArrayList<Question> questions = new ArrayList<>();
        int playerPoints = 0;
        //Scanner to scan keyboard input
        Scanner kb = new Scanner(System.in);

        /*
         * Create object instances of each question and puts into an
         * ArrayList
         */
        int numQuestions = Integer.parseInt(scan.nextLine());
        for(int i = 0; i < numQuestions; i++) {
            questions.add(new Question(scan.nextLine(), scan.nextLine(),
                    scan.nextLine(), scan.nextLine(), scan.nextLine(),
                    scan.nextLine()));
        }

        for (Question question : questions) {
            System.out.println("Please answer the following"
                    + " question:\n");
            printQuestion(question);
            String answer = kb.next();
            if (answer.equalsIgnoreCase(question.correctAnswer)) {
                playerPoints++;
                System.out.println("You got it right! You have "
                        + playerPoints + " point(s).\n");
            } else {
                System.out.println("Sorry, that was incorrect! You have "
                        + playerPoints + " point(s).\n");
            }
        }

        //end text
        if(playerPoints > numQuestions/2) {
            System.out.print("Well done! You scored "
                    + playerPoints + " points.\n");
        } else {
            System.out.print("Nice try! You scored "
                    + playerPoints + " points.\n");
        }

    }

    public static void printQuestion(Question question) {
        System.out.println(question.question);
        System.out.println(question.answerOne);
        System.out.println(question.answerTwo);
        System.out.println(question.answerThree);
        System.out.println(question.answerFour);
    }
}