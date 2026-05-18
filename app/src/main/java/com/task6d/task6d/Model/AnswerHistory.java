package com.task6d.task6d.Model;

public class AnswerHistory {
    String answerText;
    boolean isCorrectAnswer;
    boolean isUserAnswer;

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public boolean isUserAnswer() {
        return isUserAnswer;
    }

    public void setUserAnswer(boolean userAnswer) {
        isUserAnswer = userAnswer;
    }
}
