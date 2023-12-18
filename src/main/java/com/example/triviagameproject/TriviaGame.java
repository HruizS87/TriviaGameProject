package com.example.triviagameproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

public class TriviaGame extends Application {

    private Stage window;
    private Scene gameScene;
    private List<Question> questions;
    private LinkedList<Answer> answers;
    private int currentQuestionIndex;
    private Question currentQuestion;
    private User currentUser;
    private Label questionLabel = new Label();
    private Button[] answerButtons = new Button[4];
    private Label countdownLabel = new Label();
    private int questionIndex = 0;
    private Label timestampLabel = new Label();

    private DatabaseHandler databaseHandler;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Trivia Game");

        // Load questions from file
        FileHandler fileHandler = new FileHandler("input.txt");
        try {
            questions = fileHandler.loadQuestions();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        databaseHandler = new DatabaseHandler();

        // Create UI components
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(8);
        layout.setHgap(10);

        TextField nameField = new TextField("Enter your name");
        GridPane.setConstraints(nameField, 0, 0);


        GridPane.setConstraints(questionLabel, 0, 1);
        GridPane.setConstraints(countdownLabel, 2, 0);

        timestampLabel = new Label("Timestamp will appear here");
        GridPane.setConstraints(timestampLabel, 2, 1);

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new Button("Answer " + (i + 1));
            GridPane.setConstraints(answerButtons[i], i, 2);
            int index = i;
            answerButtons[i].setOnAction(event -> handleAnswer(index));
        }

        Button generateReportButton = new Button("Generate Report");
        GridPane.setConstraints(generateReportButton, 0, 3);
        generateReportButton.setOnAction(event -> generateReport());

        layout.getChildren().addAll(nameField, questionLabel, countdownLabel, timestampLabel, generateReportButton);
        layout.getChildren().addAll(Arrays.asList(answerButtons));

        nameField.setOnAction(event -> startGame(nameField.getText()));

        gameScene = new Scene(layout, 500, 300);

        window.setScene(gameScene);
        window. show();
    }

    private void handleAnswer(Integer index) {
        String selectedAnswer = null;
        if(index != null) {
            selectedAnswer = answerButtons[index].getText();
        } else {
            selectedAnswer = "No answer"; // or whatever default you would like to set
        }

        String correctAnswer = currentQuestion.getCorrectAnswer();

        boolean isCorrect = correctAnswer.trim().equalsIgnoreCase(selectedAnswer.trim()) &&
                (correctAnswer.endsWith("f") == selectedAnswer.endsWith("f"));

        if (isCorrect) {
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect");
        }

        // Create an Answer object
        Answer answer = new Answer(selectedAnswer, isCorrect);

        // Add the Answer object to the answers list
        answers.add(answer);

        // Update the timestampLabel with the timestamp from the Answer object
        timestampLabel.setText(answer.getTimestamp().toString());

        // If the answer is correct, increment the user's score
        if (isCorrect) {
            currentUser.incrementScore();
        }

        // Proceed to next question
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            currentQuestion = questions.get(currentQuestionIndex);
            loadQuestion();
        } else {
            endGame();
        }
    }


    private void generateReport() {
        List<User> users = databaseHandler.getTopScores();
        try {
            FileWriter writer = new FileWriter("report.txt");

            for (User user : users) {
                writer.write("User: " + user.getName() + "\n");
                writer.write("Score: " + user.getScore() + "\n");
                writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Report generated.");
    }

    private void startGame(String userName) {
        currentUser = new User(userName, 0);
        answers = new LinkedList<>(); // change ArrayList to LinkedList
        currentQuestionIndex = 0;
        currentQuestion = questions.get(currentQuestionIndex);
        loadQuestion();
    }

    private void handleTimeout() {
        System.out.println("Countdown has ended.");
        handleAnswer(null); // handleAnswer will take care of advancing to the next question
    }


    private Timeline countdown;
    private void loadQuestion() {
        Object obj1 = currentQuestion.getObject1();
        Object obj2 = currentQuestion.getObject2();

        questionLabel.setText(obj1 + " + " + obj2 + " = ?");
        List<String> answers = currentQuestion.getChoices();
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(answers.get(i));
        }

        if(countdown != null) {
            countdown.stop();
        }

        // If all questions have been answered, end the game.
        if (currentQuestionIndex >= questions.size()) {
            endGame();
            return;
        }

        // Load the question and answers...

        // Restart the countdown.
        countdown = new Timeline();
        countdown.setCycleCount(6);
        for (int i = 5; i >= 0; i--) {
            final int j = i;
            countdown.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(5 - i),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    countdownLabel.setText(String.valueOf(j));
                                    if (j == 0) {
                                        handleTimeout(); // Handle the timeout here
                                    }
                                }
                            })
            );
        }
        countdown.play();
    }


    private void nextQuestion() {
        if (questionIndex < questions.size() - 1) {
            questionIndex++;
            currentQuestion = questions.get(questionIndex);
            loadQuestion();
        } else {
            endGame();
        }
    }

    private void endGame() {

        if(countdown != null) {
            countdown.stop();
            countdown = null;
        }

        questionLabel.setText("Game Over! Your score is: " + currentUser.getScore());
        for (Button button : answerButtons) {
            button.setDisable(true);
        }

        System.out.println("Game ended. Popping the stack: ");
        while(!answers.isEmpty()){
            Answer answer = answers.removeLast(); // pops the 'top' element from the 'stack'
            System.out.println("Timestamp: " + answer.getTimestamp() + ", Points: " + (answer.isCorrect() ? 1 : 0));
        }

        // save the user's score to the database
        databaseHandler.storeUserData(currentUser.getName(), currentUser.getScore());
        generateReport(); // this line was added to generate the report at the end of the game
    }
}