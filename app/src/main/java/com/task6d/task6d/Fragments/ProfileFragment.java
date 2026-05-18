package com.task6d.task6d.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.Data.Entity.User;
import com.task6d.task6d.R;

import java.util.List;


public class ProfileFragment extends Fragment {

    ImageButton backButton;
    TextView usernameText;
    TextView emailText;
    TextView correctQuestions;
    TextView totalQuestions;
    TextView incorrectQuestions;
    Button shareButton;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton = view.findViewById(R.id.back_button);
        usernameText = view.findViewById(R.id.username_text);
        emailText = view.findViewById(R.id.email_text);
        correctQuestions = view.findViewById(R.id.correct_questions);
        totalQuestions = view.findViewById(R.id.total_questions);
        incorrectQuestions = view.findViewById(R.id.incorrect_questions);
        shareButton = view.findViewById(R.id.share_button);


        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        User user = db.userDAO().getActiveUser();

        List<Integer> taskIds = db.taskDAO().getUserTasksId(user.id);

        List<Question> correctQuestion = db.questionDAO().getCorrectAnswers(taskIds);
        List<Question> inCorrectQuestion = db.questionDAO().getIncorrectAnswers(taskIds);


        int correctQuestionTotal = correctQuestion.size();
        int inCorrectQuestionsTotal = inCorrectQuestion.size();


        usernameText.setText(user.getUserName());
        emailText.setText(user.getEmail());
        correctQuestions.setText(String.valueOf(correctQuestionTotal));
        incorrectQuestions.setText(String.valueOf(inCorrectQuestionsTotal));
        totalQuestions.setText(String.valueOf(correctQuestionTotal + inCorrectQuestionsTotal));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://192.168.50.179:5000/user?id=" + String.valueOf(user.id);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share your profile");
                requireContext().startActivity(shareIntent);

            }
        });
    }
}