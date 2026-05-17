package com.example.task6d.Data.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public int answerId;
    int questionId;
    String answerText;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
