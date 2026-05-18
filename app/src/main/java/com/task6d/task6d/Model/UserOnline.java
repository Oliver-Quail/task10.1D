package com.task6d.task6d.Model;

public class UserOnline {
    public int id;
    String userName;
    int correctAnswers;
    int incorrectAnswers;
    int totalQuestionsAnswer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getTotalQuestionsAnswer() {
        return totalQuestionsAnswer;
    }

    public void setTotalQuestionsAnswer(int totalQuestionsAnswer) {
        this.totalQuestionsAnswer = totalQuestionsAnswer;
    }
}
