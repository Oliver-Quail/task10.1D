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
import android.widget.ImageView;
import android.widget.TextView;

import com.task6d.task6d.Adapters.TaskAdapater;
import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Interest;
import com.task6d.task6d.Data.Entity.Task;
import com.task6d.task6d.Data.Entity.User;
import com.task6d.task6d.Data.Entity.UserInterest;
import com.task6d.task6d.R;

import java.util.ArrayList;
import java.util.Random;


public class HomeFragment extends Fragment {

    NavController navController;
    TextView nameText;
    TextView numberOfTasksText;
    Button generateTaskButton;
    ImageView profileButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView taskRecycler = view.findViewById(R.id.task_recycler);
        nameText = view.findViewById(R.id.name_text);
        numberOfTasksText = view.findViewById(R.id.number_of_tasks_text);
        generateTaskButton = view.findViewById(R.id.generate_task_button);
        profileButton = view.findViewById(R.id.profile_button);

        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        User activeUser = db.userDAO().getActiveUser();

        ArrayList<Task> tasks = new ArrayList<>(db.taskDAO().getUserTasks(activeUser.id));

        TaskAdapater taskAdapater = new TaskAdapater(requireContext(), tasks);

        taskRecycler.setAdapter(taskAdapater);


        nameText.setText(activeUser.getUserName());

        if(tasks.isEmpty()) {
            numberOfTasksText.setText("All tasks complete");
        }
        else  {
            numberOfTasksText.setText("You have "+ String.valueOf(tasks.size())  + " task due soon");
        }

        generateTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task newTask = new Task();

                ArrayList<UserInterest> interests = new ArrayList<>(db.userInterestDAO().getUserInterest(activeUser.id));

                Random rand = new Random();
                int topicId = interests.get(rand.nextInt(interests.size())).interestId;

                newTask.setAssignedTo(activeUser.id);
                newTask.setTopicId(topicId);
                newTask.setComplete(false);

                Interest interest = db.interestDAO().getInterest(topicId);

                newTask.setTaskName("Generated " + interest.getName() + " Task");
                newTask.setTaskDescription("A task about " + interest.getName());

                db.taskDAO().createTask(newTask);

                ArrayList<Task> tasks = new ArrayList<>(db.taskDAO().getUserTasks(activeUser.id));

                TaskAdapater taskAdapater = new TaskAdapater(requireContext(), tasks);

                taskRecycler.setAdapter(taskAdapater);


            }
        });

        Button upgradeButton = view.findViewById(R.id.upgrade_button);

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.upgrade_fragment);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_fragment);
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }
}