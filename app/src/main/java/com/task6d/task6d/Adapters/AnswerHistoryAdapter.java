package com.task6d.task6d.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.Model.AnswerHistory;
import com.task6d.task6d.Model.AnswersRadio;
import com.task6d.task6d.R;

import java.util.ArrayList;

public class AnswerHistoryAdapter extends RecyclerView.Adapter<AnswerHistoryAdapter.AnswerHolder>  {
    Context context;
    ArrayList<AnswerHistory> answers;

    public AnswerHistoryAdapter(Context context, ArrayList<AnswerHistory> answers) {
        this.context = context;
        this.answers = answers;
    }

    @NonNull
    @Override
    public AnswerHistoryAdapter.AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_history_answer, parent, false);
        return new AnswerHistoryAdapter.AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerHistoryAdapter.AnswerHolder holder, int position) {
        holder.answerText.setText(answers.get(position).getAnswerText());
        if(answers.get(position).isUserAnswer()) {
            holder.answerText.setTextColor(context.getResources().getColor(R.color.md_theme_error));
        }
        if(answers.get(position).isCorrectAnswer()) {
            holder.answerText.setTextColor(context.getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public static class AnswerHolder extends RecyclerView.ViewHolder {

        TextView answerText;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            answerText = itemView.findViewById(R.id.answer_text);



        }
    }
}
