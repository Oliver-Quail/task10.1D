package com.task6d.task6d.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.Model.AnswersRadio;
import com.task6d.task6d.R;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder>  {
    Context context;
    ArrayList<AnswersRadio> answers;

    public AnswerAdapter(Context context, ArrayList<AnswersRadio> answers) {
        this.context = context;
        this.answers = answers;
        Log.d("AnswerAdapter", "Adapter is running");
    }

    @NonNull
    @Override
    public AnswerAdapter.AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_answer, parent, false);
        return new AnswerAdapter.AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.AnswerHolder holder, int position) {


        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        holder.radioInput.setText(answers.get(position).getAnswerText());

        holder.radioInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question questionToUpdate = db.questionDAO().getQuestionByQuestionId(answers.get(holder.getBindingAdapterPosition()).getQuestionId());
                questionToUpdate.setUserAnswer(answers.get(holder.getBindingAdapterPosition()).answerId);
                db.questionDAO().updateQuestion(questionToUpdate);

                for(int index = 0; index < answers.size(); index++) {
                    answers.get(index).setChecked(false);
                }

                answers.get(holder.getBindingAdapterPosition()).setChecked(true);


                notifyDataSetChanged();


            }
        });

        holder.radioInput.setChecked(answers.get(holder.getBindingAdapterPosition()).isChecked());

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public static class AnswerHolder extends RecyclerView.ViewHolder {

        RadioButton radioInput;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            radioInput = itemView.findViewById(R.id.radio_input);



        }
    }
}
