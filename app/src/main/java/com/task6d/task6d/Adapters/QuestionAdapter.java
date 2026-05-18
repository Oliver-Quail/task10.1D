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

import com.task6d.task6d.BottomSheets.PromptResponseBottomSheet;
import com.task6d.task6d.Data.Entity.Answer;
import com.task6d.task6d.Model.AnswersRadio;
import com.task6d.task6d.Model.QuestionAnswer;
import com.task6d.task6d.R;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    Context context;
    ArrayList<QuestionAnswer> questionAnswers;

    public QuestionAdapter(Context context, ArrayList<QuestionAnswer> questionAnswers) {
        this.context = context;
        this.questionAnswers = questionAnswers;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_question, parent, false);
        return new QuestionAdapter.QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionHolder holder, int position) {




        holder.questionText.setText(questionAnswers.get(position).getQuestionText());
        holder.questionNumberText.setText("Question " + String.valueOf(position + 1));

        String correctAnswerText = "";

        ArrayList<Answer> answers = questionAnswers.get(holder.getBindingAdapterPosition()).getAnswers();

        ArrayList<AnswersRadio> answersRadios = new ArrayList<>();

        for(Answer answer : answers) {
            AnswersRadio answersRadio = new AnswersRadio();
            answersRadio.setAnswerText(answer.getAnswerText());
            answersRadio.setQuestionId(answer.getQuestionId());
            answersRadio.answerId = answer.answerId;
            answersRadio.setChecked(false);
            answersRadios.add(answersRadio);

            if(questionAnswers.get(position).getCorrectAnswer() == answer.answerId) {
                correctAnswerText = answer.getAnswerText();
            }
        }

        final String correctAnswer = correctAnswerText;


        AnswerAdapter answerAdapter = new AnswerAdapter(context, answersRadios);


        holder.radioRecycler.setAdapter(answerAdapter);

        holder.hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptResponseBottomSheet sheet = new PromptResponseBottomSheet();

                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putString("mode", "hint");
                fragmentBundle.putString("correctAnswer", correctAnswer);
                fragmentBundle.putString("question", questionAnswers.get(holder.getBindingAdapterPosition()).getQuestionText());

                sheet.setArguments(fragmentBundle);

                sheet.show(((AppCompatActivity)context).getSupportFragmentManager(), "sheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionAnswers.size();
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder {

        TextView questionNumberText;
        TextView questionText;
        RecyclerView radioRecycler;
        TextView hintText;
        Button hintButton;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberText = itemView.findViewById(R.id.question_number_text);
            questionText = itemView.findViewById(R.id.question_text);
            radioRecycler = itemView.findViewById(R.id.radio_recycler);
            hintText = itemView.findViewById(R.id.hint_text);
            hintButton = itemView.findViewById(R.id.hint_button);
        }
    }
}
