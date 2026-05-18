package com.task6d.task6d.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task6d.task6d.BottomSheets.PromptResponseBottomSheet;
import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Answer;
import com.task6d.task6d.Data.Entity.Question;
import com.task6d.task6d.Model.AnswerHistory;
import com.task6d.task6d.Model.AnswersRadio;
import com.task6d.task6d.Model.QuestionAnswer;
import com.task6d.task6d.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionHistoryAdapter extends RecyclerView.Adapter<QuestionHistoryAdapter.QuestionHolder> {
    Context context;
    ArrayList<Question> questions;

    public QuestionHistoryAdapter(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionHistoryAdapter.QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_question_history, parent, false);
        return new QuestionHistoryAdapter.QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHistoryAdapter.QuestionHolder holder, int position) {


        holder.questionText.setText(questions.get(position).getQuestion());
        holder.questionNumberText.setText("Question " + String.valueOf(position + 1));

        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        List<Answer> answers = db.answerDAO().getAnswersByQuestion(questions.get(position).questionId);

        String correctAnswerText = "";


        ArrayList<AnswerHistory> answersRadios = new ArrayList<>();

        for(Answer answer : answers) {
            AnswerHistory answersRadio = new AnswerHistory();
            if(answer.answerId == questions.get(position).getCorrectAnswer()) {
                answersRadio.setCorrectAnswer(true);
            }
            else {
                answersRadio.setCorrectAnswer(false);
            }
            if(answer.answerId == questions.get(position).getUserAnswer()) {
                answersRadio.setUserAnswer(true);

            }
            else {
                answersRadio.setUserAnswer(false);
            }
            answersRadio.setAnswerText(answer.getAnswerText());
            answersRadios.add(answersRadio);

        }

        AnswerHistoryAdapter answerAdapter = new AnswerHistoryAdapter(context, answersRadios);


        holder.radioRecycler.setAdapter(answerAdapter);


    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView questionNumberText;
        TextView questionText;
        RecyclerView radioRecycler;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberText = itemView.findViewById(R.id.question_number_text);
            questionText = itemView.findViewById(R.id.question_text);
            radioRecycler = itemView.findViewById(R.id.radio_recycler);
        }
    }
}
