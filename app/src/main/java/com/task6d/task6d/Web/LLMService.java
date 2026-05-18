package com.task6d.task6d.Web;

import com.task6d.task6d.Model.LLMResponse;
import com.task6d.task6d.Model.QuestionRaw;
import com.task6d.task6d.Model.UserOnline;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LLMService {

    @GET("getQuiz")
    Call<QuestionRaw> getQuestions(@Query("topic") String topic);

    @GET("explainAnswer")
    Call<LLMResponse> getExplanation(@Query("question") String question, @Query("correct") String correctAnswer, @Query("incorrect") String incorrectAnswer);

    @GET("getHint")
    Call<LLMResponse> getHint(@Query("question") String question, @Query("answer") String correctAnswer);

    @FormUrlEncoded
    @POST("addUserData")
    Call<Void> addUser(@Field("userName") String userName, @Field("id") int id, @Field("correctAnswers") int correctAnswers, @Field("incorrectAnswers") int incorrectAnswers, @Field("totalQuestionsAnswer") int totalQuestionsAnswer);
}
