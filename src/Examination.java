import java.io.*;
import java.util.*;

public class Examination {
    private QuestionBank questionBank;          // Stores the question bank for the examination
    private List<Question> currentQuestions;    // List of questions for the current exam session
    private List<Character> studentAnswers;     // List of student's answers for the current exam
    private int totalCorrect;                   // Counter for total correct answers
    private String studentId;                   // Stores the student's ID
    private String examDate;                    // Stores the date of the exam

    // Constructor initializes the examination with a specific question bank and resets other fields.
    public Examination() {
        this.questionBank = new QuestionBank("Java Programming");
        this.currentQuestions = new ArrayList<>();
        this.studentAnswers = new ArrayList<>();
        this.totalCorrect = 0;
    }

    public static void main(String[] args) {
        Examination exam = new Examination();    // Create a new Examination instance
        try {
            exam.questionBank.loadQuestions("questionbanks.txt");    // Load questions from file
        } catch (IOException e) {
            System.out.println("Error loading questions: " + e.getMessage());
            return;    // Exit if questions cannot be loaded
        }
        int choice = -1;
        Scanner scanner = new Scanner(System.in);    // Create a Scanner for user input
        try {
            do {
                choice = getUserChoice(scanner);    // Get user choice
                handleUserChoice(exam, scanner, choice);    // Handle the user's choice
            } while (choice != 3);    // Continue until the user chooses to exit
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();    // Close the scanner
        }
    }

    // Get user choice for the menu
    private static int getUserChoice(Scanner scanner) {
        System.out.println("Examination");
        System.out.println("1) Start Examination");
        System.out.println("2) Export Results");
        System.out.println("3) Exit");
        System.out.print("Choose an option: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid option. Please enter a number.");
            scanner.next(); // Consume the invalid input
            System.out.print("Choose an option: ");
        }
        return scanner.nextInt();
    }

    // Handle user choice from the menu
    private static void handleUserChoice(Examination exam, Scanner scanner, int choice) {
        scanner.nextLine();  // Consume newline
        System.out.println();
        switch (choice) {
            case 1 -> exam.startExamination(scanner);    // Start examination
            case 2 -> exam.exportResults();              // Export results
            case 3 -> System.out.println("Thank you for taking the test!");    // Exit message
            default -> System.out.println("Invalid option. Please try again.");    // Invalid option message
        }
    }

    // Get the exam date from the user
    private void getExamDate(Scanner scanner) {
        System.out.print("Enter exam date: ");
        this.examDate = scanner.nextLine();
    }
    
    // Get the student ID from the user
    private void getStudentId(Scanner scanner) {
        System.out.print("Enter your student ID: ");
        this.studentId = scanner.nextLine();
    }

    // Get the number of questions for the exam based on difficulty level
    private int getNumberOfQuestions(Scanner scanner) {
        System.out.print("Level 1: 10 questions\nLevel 2: 20 questions\nLevel 3: 30 questions\nChoose Level (1, 2, 3): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input type. Please enter a number from 1 to 3 only.");
            System.out.println();
            scanner.next(); // Consume the invalid input
            System.out.print("Choose Level (1, 2, 3): ");
        }
        int level = scanner.nextInt();
        if (level >= 1 && level <= 3) {
            return level;
        } else {
            System.out.println("Invalid level. Please enter the level value from 1 to 3 only.");
            System.out.println();
            return getNumberOfQuestions(scanner);
        }
    }

    // Start the examination process
    private void startExamination(Scanner scanner) {
        this.totalCorrect = 0;
        this.studentAnswers.clear();

        getExamDate(scanner);    // Get exam date from the user
        int numberOfQuestions = getNumberOfQuestions(scanner) * 10;    // Get number of questions based on chosen level
        scanner.nextLine();  // Consume newline
        getStudentId(scanner);    // Get student ID from the user

        this.currentQuestions = questionBank.getQuestions(numberOfQuestions);    // Fetch questions from the question bank
        if (currentQuestions.size() < numberOfQuestions) {
            System.out.println("Not enough questions available. Proceeding with " + currentQuestions.size() + " questions.");
        }

        administerQuestions(scanner);    // Administer the questions to the user
        displayTestStats();    // Display the test statistics
    }

    // Administer questions to the user
    private void administerQuestions(Scanner scanner) {
        for (int i = 0; i < currentQuestions.size(); i++) {
            Question q = currentQuestions.get(i);
            System.out.println("Question " + (i + 1) + "/" + currentQuestions.size());
            System.out.println(q.getQuestionText());
            String[] options = q.getOptions();
            
            // Trim the options and calculate the maximum length
            int maxLength = 0;
            for (int j = 0; j < options.length; j++) {
                options[j] = options[j].trim();  // Trim leading and trailing spaces
                if (options[j].length() > maxLength) {
                    maxLength = options[j].length();
                }
            }

            // Display options in two columns with dynamic spacing
            for (int j = 0; j < options.length; j += 2) {
                if (j + 1 < options.length) {
                    System.out.printf("%c. %-" + maxLength + "s   %c. %s%n", (char) ('a' + j), options[j], (char) ('a' + j + 1), options[j + 1]);
                } else {
                    System.out.printf("%c. %s%n", (char) ('a' + j), options[j]);
                }
            }

            System.out.print("\nEnter your answer: ");
            char answer;
            try {
                String input = scanner.nextLine().toLowerCase();
                if (input.length() != 1 || input.charAt(0) < 'a' || input.charAt(0) > 'd') {
                    throw new InvalidAnswerException("Invalid answer. Please enter a valid option (a, b, c, or d).");
                }
                answer = input.charAt(0);

            } catch (InputMismatchException | StringIndexOutOfBoundsException | InvalidAnswerException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                i--; // Retry the same question
                continue;
            }
            studentAnswers.add(answer);    // Add the student's answer to the list
            if (answer == q.getActualAnswer()) {
                totalCorrect++;    // Increment the total correct answers if the answer is correct
            }
        }
    }

    // Display the test statistics
    private void displayTestStats() {
        int totalQuestions = currentQuestions.size();
        int totalIncorrect = totalQuestions - totalCorrect;
        double score = ((double) totalCorrect / totalQuestions) * 100;
        char grade = getGrade(score);

        System.out.println("\nTest Statistics:");
        System.out.println("Total Correct: " + totalCorrect);
        System.out.println("Total Incorrect: " + totalIncorrect);
        System.out.printf("Overall Mark: %.2f%% (%c)\n", score, grade);
        System.out.println();
    }

    // Get the grade based on the score
    private char getGrade(double score) {
        if (score >= 90) {
            return 'A';
        } else if (score >= 80) {
            return 'B';
        } else if (score >= 70) {
            return 'C';
        } else if (score >= 60) {
            return 'D';
        } else {
            return 'E';
        }
    }

    // Export the exam results to a text file
    private void exportResults() {
        int totalQuestions = currentQuestions.size();
        double score = ((double) totalCorrect / totalQuestions) * 100;
        char grade = getGrade(score);

        try (PrintWriter writer = new PrintWriter(new File(this.studentId + ".txt"))) {
            writer.println("Examination: Java Programming Lab Test " + this.examDate);
            writer.println("Student Answers:");
            writer.println(this.studentId);
            for (int i = 0; i < currentQuestions.size(); i++) {
                char studentAnswer = studentAnswers.get(i);
                char correctAnswer = currentQuestions.get(i).getActualAnswer();
                if (studentAnswer != correctAnswer) {
                    writer.printf("%2d) %c (%c)%n", i + 1, studentAnswer, correctAnswer);
                } else {
                    writer.printf("%2d) %c%n", i + 1, studentAnswer);
                }
            }
            writer.printf("%n(%s): %.2f%% %c%n", this.studentId, score, grade);
            System.out.println("Results exported to " + this.studentId + ".txt\n");
        } catch (FileNotFoundException e) {
            System.out.println("Error exporting results: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while exporting results: " + e.getMessage());
        }
    }

    // Custom exception for invalid answers
    static class InvalidAnswerException extends Exception {
        public InvalidAnswerException(String message) {
            super(message);
        }
    }
}
