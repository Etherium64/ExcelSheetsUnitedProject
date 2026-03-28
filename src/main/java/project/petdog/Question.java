package project.petdog;

public class Question {
    String question = null;
    //Answer choices
    String answerOne = null;
    String answerTwo = null;
    String answerThree = null;
    String answerFour = null;
    //Correct answer
    String correctAnswer = null;

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

    //ArrayList<Question> questions = new ArrayList<>();
    // question.add(new Question(scan.nextLine))

    //[question1, question2]
}