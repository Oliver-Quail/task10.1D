package com.task6d.task6d.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.task6d.task6d.Adapters.QuestionHistoryAdapter;
import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.R;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    RecyclerView historyRecycler;
    ImageButton backButtton;

    public HistoryFragment() {
        // Required empty public constructor
    }


    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRecycler = view.findViewById(R.id.question_recycler);
        backButtton = view.findViewById(R.id.back_button);

        int taskId = getArguments().getInt("taskId");
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        ArrayList<Question> questions = new ArrayList<Question>(db.questionDAO().getQuestions(taskId));

        QuestionHistoryAdapter questionHistoryAdapter = new QuestionHistoryAdapter(requireContext(), questions);

        historyRecycler.setAdapter(questionHistoryAdapter);

        backButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        });

    }
}