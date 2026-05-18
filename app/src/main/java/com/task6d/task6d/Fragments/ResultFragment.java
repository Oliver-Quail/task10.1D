package com.task6d.task6d.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.task6d.task6d.Adapters.ResultAdapter;
import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Answer;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.Data.Entity.Task;
import com.task6d.task6d.Model.Result;
import com.task6d.task6d.R;

import java.util.ArrayList;
import java.util.List;


public class ResultFragment extends Fragment {

    RecyclerView resultRecyler;
    Button continueButton;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
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
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        resultRecyler = view.findViewById(R.id.result_recycler);
        continueButton = view.findViewById(R.id.continue_button);
        int taskId = getArguments().getInt("taskId");

        List<Question> questions = db.questionDAO().getQuestions(taskId);

        ArrayList<Result> results = new ArrayList<>();

        for(Question question : questions) {
            Result newResult = new Result();
            newResult.setQuestionText(question.getQuestion());

            List<Answer> answers = db.answerDAO().getAnswersByQuestion(question.questionId);

            for(Answer answer : answers) {
                if(answer.answerId == question.getUserAnswer()) {
                    newResult.setUserAnswer(answer.getAnswerText());
                }
                if(answer.answerId == question.getCorrectAnswer()) {
                    newResult.setCorrectAnswer(answer.getAnswerText());
                    if(answer.answerId == question.getCorrectAnswer()) {
                        newResult.setCorrect(true);
                        break;
                    }
                    else {
                        newResult.setCorrect(false);
                    }
                }
            }

            results.add(newResult);
        }

        ResultAdapter resultAdapter = new ResultAdapter(requireContext(), results);

        resultRecyler.setAdapter(resultAdapter);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = db.taskDAO().getTaskById(taskId);
                task.setComplete(true);
                db.taskDAO().updateTask(task);

                NavController navController = Navigation.findNavController(view);

                navController.navigate(R.id.home_fragment);
            }
        });


        return view;
    }
}