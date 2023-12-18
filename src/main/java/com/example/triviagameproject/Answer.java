package com.example.triviagameproject;

import java.time.LocalDateTime;

public class Answer {
    
    private String selectedAnswer; 
    private LocalDateTime timestamp; 
    private boolean isCorrect; 

    /**
     * Constructor for Answer class.
     * Initializes the selectedAnswer, timestamp, and isCorrect fields.
     * 
     * @param selectedAnswer The answer selected by the user.
     * @param isCorrect      Boolean value indicating if the selected answer is correct.
     */
    public Answer(String selectedAnswer, boolean isCorrect) {
        this.selectedAnswer = selectedAnswer;
        this.timestamp = LocalDateTime.now(); // Assign current time as the timestamp.
        this.isCorrect = isCorrect;
    }

 
    /**
     * Returns the selected answer.
     * 
     * @return A string representing the user's selected answer.
     */
    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    /**
     * Returns the timestamp of when the answer was selected.
     * 
     * @return A LocalDateTime object representing the timestamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if the selected answer is correct.
     * 
     * @return A boolean value, true if the answer is correct, otherwise false.
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    // Note: No setters are defined as the fields are initialized in the constructor and are not expected to change.
}
