package com.example.task6d.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.task6d.BottomSheets.PromptResponseBottomSheet;
import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.Question;
import com.example.task6d.Model.AnswersRadio;
import com.example.task6d.Model.Result;
import com.example.task6d.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {
    Context context;
    ArrayList<Result> results;

    public ResultAdapter(Context context, ArrayList<Result> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ResultAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_result, parent, false);
        return new ResultAdapter.ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultHolder holder, int position) {

        holder.questionText.setText(results.get(position).getQuestionText());
        holder.userAnswerText.setText("Your answer: " + results.get(position).getUserAnswer());

        if(results.get(position).isCorrect()) {
            holder.resultText.setText("You are correct");
        }
        else {
            holder.resultText.setText("You are incorrect");
            holder.correctAnswerText.setText("Correct answer: " +results.get(position).getCorrectAnswer());
        }

        holder.explainAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptResponseBottomSheet sheet = new PromptResponseBottomSheet();

                Bundle fragmentBundle = new Bundle();

                fragmentBundle.putString("mode", "explain");
                fragmentBundle.putString("correctAnswer", results.get(holder.getBindingAdapterPosition()).getCorrectAnswer());
                fragmentBundle.putString("incorrectAnswer", results.get(holder.getBindingAdapterPosition()).getUserAnswer());
                fragmentBundle.putString("question", results.get(holder.getBindingAdapterPosition()).getQuestionText());

                sheet.setArguments(fragmentBundle);
                sheet.show(((AppCompatActivity)context).getSupportFragmentManager(), "sheet");


            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultHolder extends RecyclerView.ViewHolder {

        TextView questionText;
        TextView resultText;
        TextView correctAnswerText;
        TextView userAnswerText;
        Button explainAnswerButton;

        public ResultHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.question_text);
            resultText = itemView.findViewById(R.id.result_text);
            correctAnswerText = itemView.findViewById(R.id.correct_answer_text);
            userAnswerText = itemView.findViewById(R.id.user_answer_text);
            explainAnswerButton = itemView.findViewById(R.id.explain_answer_button);

        }
    }
}
