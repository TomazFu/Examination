import java.io.*;
import java.util.*;

public class QuestionBank {
    // Private member variables
    private List<Question> questions; // Holds a list of Question objects
    private String title; // Holds the title of the question bank

    // Constructor to initialize the QuestionBank object
    public QuestionBank(String title) {
        this.questions = new ArrayList<>(); // Initialize the questions list
        this.title = title; // Initialize the title
    }

    // Method to load questions from a file
    public void loadQuestions(String file) throws IOException {
        FileReader fr = new FileReader(file); // Create a FileReader to read the file
        Scanner sc = new Scanner(fr); // Create a Scanner to read the file content

        boolean isOptionSection = false; // Flag to check if the current section is options
        String questionText = ""; // To store the question text
        int i = 0; // Option index
        String[] options = new String[4]; // Array to store options
        char actualAnswer = ' '; // To store the correct answer
        String line = ""; // To store each line read from the file
        boolean multipleLines = false; // Flag to handle multi-line questions

        // Loop through each line in the file
        while (sc.hasNext()) {
            line = sc.nextLine(); // Read the next line
            // Check if the line starts with an option indicator (e.g., "a." or "*a.")
            if (line.startsWith("a.") || line.startsWith("*a.")) {
                isOptionSection = true; // Set flag to true
            }

            // If currently in the option section
            if (isOptionSection) {
                // Check if the option is marked as the correct answer
                if (line.charAt(0) == '*') {
                    actualAnswer = line.charAt(1); // Set the correct answer
                    line = line.substring(1); // Remove the '*' character
                }
                options[i] = line.substring(2); // Store the option text
                i++; // Increment the option index
            } else {
                // If handling multi-line questions
                if (multipleLines == true) {
                    questionText += "\n"; // Add a newline character
                }
                questionText += line; // Append the line to the question text
                multipleLines = true; // Set the flag to true
            }

            // If all 4 options have been read
            if (i == 4) {
                Question question = new Question(questionText, options, actualAnswer); // Create a new Question object
                questions.add(question); // Add the question to the list
                i = 0; // Reset the option index
                questionText = ""; // Reset the question text
                options = new String[4]; // Reset the options array
                isOptionSection = false; // Reset the flag
                multipleLines = false; // Reset the flag
            }
        }
    }

    // Method to get a specified number of questions randomly
    public List<Question> getQuestions(int number) {
        if (number > questions.size()) {
            number = questions.size(); // Adjust to available questions if requested number is too large
        }
        Collections.shuffle(questions); // Shuffle the list to get random questions
        return questions.subList(0, number); // Return the specified number of questions
    }

    // Method to display the details of the question bank
    public void showQuestionsDetails() {
        System.out.println("Title: " + title); // Print the title
        System.out.println("Total questions: " + questions.size()); // Print the total number of questions
    }

    // Method to clear the question bank
    public void resetQuestionBank() {
        questions.clear(); // Clear the list of questions
    }

    // Method to view questions with pagination
    public void viewQuestions(Scanner scanner) {
        int pageSize = 3; // Number of questions to display per page
        for (int i = 0; i < questions.size(); i += pageSize) {
            for (int j = i; j < i + pageSize && j < questions.size(); j++) {
                Question q = questions.get(j); // Get the current question
                System.out.println(q.getQuestionText()); // Print the question text
                for (int k = 0; k < q.getOptions().length; k++) {
                    System.out.println((char) ('a' + k) + ")" + q.getOptions()[k]); // Print the options
                }
                System.out.println(); // Add a blank line between questions
            }
            System.out.println("Press Enter to continue..."); // Prompt the user to continue
            scanner.nextLine(); // Wait for user input
        }
    }

    // Method to get the size of the question bank
    public int getQuestionBankSize() {
        return questions.size(); // Return the number of questions
    }
}
