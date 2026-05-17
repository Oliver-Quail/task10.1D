package com.example.task6d.BottomSheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task6d.Model.LLMResponse;
import com.example.task6d.R;
import com.example.task6d.Web.LLMService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromptResponseBottomSheet extends BottomSheetDialogFragment {

    Button closeButton;
    LinearLayout loadingHolder;
    LinearLayout sheetContent;
    TextView promptText;
    TextView LLLMResponseText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_prompt, container, false);

        closeButton = view.findViewById(R.id.close_button);
        loadingHolder = view.findViewById(R.id.loading_holder);
        sheetContent = view.findViewById(R.id.sheet_content);
        promptText = view.findViewById(R.id.prompt_text);
        LLLMResponseText = view.findViewById(R.id.llm_reponse_text);

        String mode = getArguments().getString("mode");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.50.179:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(90, TimeUnit.SECONDS).build()).build();

        LLMService service = retrofit.create(LLMService.class);
        Call<LLMResponse> result = null;

        switch (mode) {
            case "hint":
                result = service.getHint(getArguments().getString("question"), getArguments().getString("correctAnswer"));
            break;

            case "explain":
                result = service.getExplanation(getArguments().getString("question"), getArguments().getString("correctAnswer"), getArguments().getString("incorrectAnswer"));

            break;
        }

        if(result == null) {
            dismiss();
        }

        result.enqueue(new Callback<LLMResponse>() {
            @Override
            public void onResponse(Call<LLMResponse> call, Response<LLMResponse> response) {
                if (response.isSuccessful() && response.body() != null) {


                    promptText.setText(response.body().getPrompt());
                    LLLMResponseText.setText(response.body().getContent());
                    Log.d("BottomSheet", response.body().getPrompt());
                    loadingHolder.setVisibility(View.GONE);
                    sheetContent.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(requireContext(), "Server issues, please try again later", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<LLMResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });




        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;

    }
}
