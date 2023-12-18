package com.example.triviagameproject;

import java.util.List;

public class Question {
    private String object1; // First part of the question
    private String object2; // Second part of the question
    private String correctAnswer; // Correct answer for the question
    private List<String> choices; // List of possible answers

    /**
     * Constructor for the Question class.
     * It initializes the question parts and calculates the correct answer.
     * 
     * @param object1 The first part of the question.
     * @param object2 The second part of the question.
     * @param choices A list of possible answers for the question.
     */
    public Question(String object1, String object2, List<String> choices) {
        this.object1 = object1;
        this.object2 = object2;
        this.choices = choices;

        // Logic to calculate the correct answer based on object1 and object2
        // Explanation of the regex used for checking if object1 and object2 are numeric:
        // [-+]? - Matches an optional '+' or '-' sign at the beginning.
        // [0-9]* - Matches any sequence of digits, including none.
        // \\.? - Matches an optional decimal point. '\\' is used to escape the dot.
        // [0-9]+ - Matches a sequence of one or more digits.
        // ([eE][-+]?[0-9]+)? - An optional group for scientific notation:
        //   - [eE] matches 'e' or 'E'.
        //   - [-+]? optionally matches '+' or '-'.
        //   - [0-9]+ matches one or more digits (the exponent).
        // [f]? - Optionally matches 'f', indicating a float.
        
        if (object1.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?[f]?") && 
            object2.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?[f]?")) {
            // Handling numeric values (int, double, or float)
            if (object1.endsWith("f") || object2.endsWith("f")) {
                // Calculating the correct answer for float values
                float num1 = Float.parseFloat(object1.replace("f", ""));
                float num2 = Float.parseFloat(object2.replace("f", ""));
                this.correctAnswer = (num1 + num2) + "f";
            } else if (object1.contains(".") || object2.contains(".")) {
                // Calculating the correct answer for double values
                double num1 = Double.parseDouble(object1);
                double num2 = Double.parseDouble(object2);
                this.correctAnswer = Double.toString(num1 + num2);
            } else {
                // Calculating the correct answer for integer values
                int num1 = Integer.parseInt(object1);
                int num2 = Integer.parseInt(object2);
                this.correctAnswer = Integer.toString(num1 + num2);
            }
        } else {
            // Handling string values
            this.correctAnswer = object1 + object2;
        }
    }

    // Getter methods
    public String getObject1() {
        return object1;
    }

    public String getObject2() {
        return object2;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
