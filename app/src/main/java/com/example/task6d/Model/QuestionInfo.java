package com.example.task6d.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionInfo {
    @SerializedName("correct_answer")
    private int correctAnswer;

    @SerializedName("options")
    private List<String> answersText;

    @SerializedName("question")
    private String questionText;

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getAnswersText() {
        return answersText;
    }

    public void setAnswersText(List<String> answersText) {
        this.answersText = answersText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
