package com.example.task6d.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.task6d.Adapters.QuestionAdapter;
import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.Answer;
import com.example.task6d.Data.Entity.Interest;
import com.example.task6d.Data.Entity.Question;
import com.example.task6d.Data.Entity.Task;
import com.example.task6d.Model.QuestionAnswer;
import com.example.task6d.Model.QuestionInfo;
import com.example.task6d.Model.QuestionRaw;
import com.example.task6d.R;
import com.example.task6d.Web.LLMService;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskFragment extends Fragment {

    RecyclerView questionRecycler;
    Button submitButton;
    NavController navController;
    LinearProgressIndicator loadingIndicator;
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        questionRecycler = view.findViewById(R.id.question_recycler);
        submitButton = view.findViewById(R.id.submit_button);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
        int taskId = getArguments().getInt("taskId");

        List<Question> taskQuestion = db.questionDAO().getQuestions(taskId);


        if(taskQuestion.size() == 0) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.50.179:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().readTimeout(90, TimeUnit.SECONDS).build()).build();

            LLMService service = retrofit.create(LLMService.class);

            Task task = db.taskDAO().getTaskById(taskId);

            Interest interest = db.interestDAO().getInterest(task.getTopicId());

            Call<QuestionRaw> result = service.getQuestions(interest.getName());

            result.enqueue(new Callback<QuestionRaw>() {
                @Override
                public void onResponse(Call<QuestionRaw> call, Response<QuestionRaw> response) {
                    Log.d("API", "Result returned");
                    if (response.isSuccessful() && response.body() != null) {

                        QuestionRaw questions = response.body();

                        Log.d("API", String.valueOf(questions.getQuiz().size()));

                        for(QuestionInfo question : questions.getQuiz()) {
                            Question newQuestion = new Question();

                            newQuestion.setTaskId(taskId);
                            newQuestion.setQuestion(question.getQuestionText());
                            newQuestion.setCorrectAnswer(0);
                            newQuestion.setUserAnswer(0);

                            int questionId = (int)db.questionDAO().createQuestion(newQuestion);

                            Question createdQuestion = db.questionDAO().getQuestionByQuestionId(questionId);

                            int index = 0;
                            while (index < question.getAnswersText().size()) {
                                Answer newAnswer = new Answer();

                                newAnswer.setAnswerText(question.getAnswersText().get(index));
                                newAnswer.setQuestionId(createdQuestion.questionId);

                                int newAnswerId = (int)db.answerDAO().createAnswer(newAnswer);

                                if(index == question.getCorrectAnswer()) {
                                    createdQuestion.setCorrectAnswer(newAnswerId);
                                    db.questionDAO().updateQuestion(createdQuestion);
                                }

                                index++;
                            }

                        }

                        populateRecyclers(taskId);

                    } else {
                        Log.e("API", "Response error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<QuestionRaw> call, Throwable t) {
                    Log.d("API", "Result Failed");
                    Log.d("API", t.getMessage());



                }
            });
        }
        else {
            Log.d("TaskFragment", String.valueOf(taskId));
            populateRecyclers(taskId);
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(view);
                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putInt("taskId", taskId);
                navController.navigate(R.id.to_result_fragment, fragmentBundle);
            }
        });

        return view;
    }

    private void populateRecyclers(int taskId) {
        Log.d("TaskFragment", "question already exists");
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
        List<Question> questions = db.questionDAO().getQuestions(taskId);

        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();

        for(Question question :questions) {
            QuestionAnswer newQuestionAnswer = new QuestionAnswer();
            newQuestionAnswer.setQuestionId(question.questionId);
            newQuestionAnswer.setQuestionText(question.getQuestion());
            newQuestionAnswer.setCorrectAnswer(question.getCorrectAnswer());
            ArrayList<Answer> answers = new ArrayList<>(db.answerDAO().getAnswersByQuestion(question.questionId));
            newQuestionAnswer.setAnswers(answers);
            questionAnswers.add(newQuestionAnswer);
        }

        QuestionAdapter questionAdapter = new QuestionAdapter(requireContext(), questionAnswers);

        questionRecycler.setAdapter(questionAdapter);
        loadingIndicator.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }

}