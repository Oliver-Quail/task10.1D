package com.example.task6d.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task6d.Adapters.ResultAdapter;
import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.Answer;
import com.example.task6d.Data.Entity.Question;
import com.example.task6d.Model.Result;
import com.example.task6d.R;

import java.util.ArrayList;
import java.util.List;


public class ResultFragment extends Fragment {

    RecyclerView resultRecyler;

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




        return view;
    }
}