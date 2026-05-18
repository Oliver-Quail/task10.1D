package com.task6d.task6d.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Task;
import com.task6d.task6d.R;

import java.util.ArrayList;

public class TaskAdapater extends RecyclerView.Adapter<TaskAdapater.TaskHolder> {
    Context context;
    ArrayList<Task> tasks;

    public TaskAdapater(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskAdapater.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_task_view, parent, false);
        return new TaskAdapater.TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapater.TaskHolder holder, int position) {

        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        holder.taskTitleText.setText(tasks.get(position).getTaskName());
        holder.taskDescriptionText.setText(tasks.get(position).getTaskDescription());
        holder.taskId = tasks.get(position).taskId;
        holder.isComplete = tasks.get(position).isComplete();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {

        TextView taskTitleText;
        TextView taskDescriptionText;
        Button startTaskButton;
        int taskId;
        boolean isComplete;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            taskTitleText = itemView.findViewById(R.id.task_title_text);
            taskDescriptionText = itemView.findViewById(R.id.task_description_text);
            startTaskButton = itemView.findViewById(R.id.start_task_button);

            startTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(v);
                    Bundle fragmentBundle = new Bundle();
                    fragmentBundle.putInt("taskId", taskId);
                    if(isComplete) {
                        navController.navigate(R.id.history_task, fragmentBundle);
                    }
                    else {
                        navController.navigate(R.id.start_task, fragmentBundle);

                    }
                }
            });

        }
    }
}
