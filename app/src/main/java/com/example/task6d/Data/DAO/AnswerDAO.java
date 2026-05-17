package com.example.task6d.Data.DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task6d.Data.Entity.Answer;

import java.util.List;

@Dao
public interface AnswerDAO {

    @Insert
    long createAnswer(Answer answer);

    @Query("SELECT * FROM answer WHERE questionId = :questionId")
    List<Answer> getAnswersByQuestion(int questionId);

}
