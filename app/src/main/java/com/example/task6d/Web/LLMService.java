package com.example.task6d.Web;

import com.example.task6d.Model.LLMResponse;
import com.example.task6d.Model.QuestionRaw;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LLMService {

    @GET("getQuiz")
    Call<QuestionRaw> getQuestions(@Query("topic") String topic);

    @GET("explainAnswer")
    Call<LLMResponse> getExplanation(@Query("question") String question, @Query("correct") String correctAnswer, @Query("incorrect") String incorrectAnswer);

    @GET("getHint")
    Call<LLMResponse> getHint(@Query("question") String question, @Query("answer") String correctAnswer);
}
