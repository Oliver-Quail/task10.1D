package com.task6d.task6d.Model;

import com.google.gson.annotations.SerializedName;

public class UserOnline {

    @SerializedName("id")
    public int id;
    @SerializedName("userName")
    String userName;
    @SerializedName("correctAnswers")
    int correctAnswers;
    @SerializedName("incorrectAnswers")
    int incorrectAnswers;
    @SerializedName("totalQuestionsAnswer")
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
