package com.example.mindbloom;

public class Question {

    private String questionText;
    private String[] options;      // always 4 options
    private int correctAnswerIndex; // 0, 1, 2, or 3

    public Question(String questionText, String[] options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
}