package com.example.triviagameproject;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class FileHandler {

    private String fileName;
    private List<Question> questions;

    public FileHandler(String fileName) {
        this.fileName = fileName;
        questions = new ArrayList<>();
    }

    public List<Question> loadQuestions() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        if (lines.size() % 2 != 0) {
            throw new IllegalArgumentException("Invalid file format");
        }

        for (int i = 0; i < lines.size(); i += 2) {
            String[] objects = lines.get(i).split(" ");
            if (objects.length != 2) {
                throw new IllegalArgumentException("Invalid question format on line " + (i + 1));
            }

            String[] choices = lines.get(i + 1).split(" ");
            if (choices.length != 4) {
                throw new IllegalArgumentException("Invalid answer format on line " + (i + 2));
            }

            questions.add(new Question(objects[0], objects[1], Arrays.asList(choices)));
        }

        return questions;
    }

    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler("input.txt");
        try {
            List<Question> questions = fileHandler.loadQuestions();
            for (Question question : questions) {
                System.out.println("Object1: " + question.getObject1());
                System.out.println("Object2: " + question.getObject2());
                System.out.println("Choices: " + String.join(", ", question.getChoices()));
                System.out.println("Correct Answer: " + question.getCorrectAnswer());
                System.out.println("-----------------------------");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

