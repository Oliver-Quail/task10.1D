package com.task6d.task6d.Data.DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.task6d.task6d.Data.Entity.Question;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Insert
    long createQuestion(Question question);

    @Update
    void updateQuestion(Question question);

    @Query("SELECT * FROM question WHERE taskId = :taskId")
    List<Question> getQuestions(int taskId);

    @Query("SELECT * FROM question WHERE questionId = :questionId")
    Question getQuestionByQuestionId(int questionId);

    @Query("SELECT * FROM question WHERE correctAnswer = userAnswer AND taskId IN (:taskIds)")
    List<Question> getCorrectAnswers(List<Integer> taskIds);

    @Query("SELECT * FROM question WHERE correctAnswer != userAnswer AND taskId IN (:taskIds)")
    List<Question> getIncorrectAnswers(List<Integer> taskIds);
}
