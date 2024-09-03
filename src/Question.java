public class Question {
    // Private member variables
    private String questionText; // Holds the text of the question
    private String[] options; // Holds the possible answer options
    private char actualAnswer; // Holds the correct answer as a character

    // Constructor to initialize the Question object
    public Question(String questionText, String[] options, char actualAnswer) {
        this.questionText = questionText; // Initialize question text
        this.options = options; // Initialize options
        this.actualAnswer = actualAnswer; // Initialize actual answer
    }

    // Getter method for questionText
    public String getQuestionText() {
        return questionText; // Return the question text
    }
    
    // Getter method for options
    public String[] getOptions() {
        return options; // Return the options array
    }

    // Getter method for actualAnswer
    public char getActualAnswer() {
        return actualAnswer; // Return the correct answer
    }

    // Setter method for questionText
    public void setQuestionText(String questionText) {
        this.questionText = questionText; // Set the question text
    }

    // Setter method for options
    public void setOptions(String[] options) {
        this.options = options; // Set the options array
    }

    // Setter method for actualAnswer
    public void setActualAnswer(char actualAnswer) {
        this.actualAnswer = actualAnswer; // Set the correct answer
    }
}
