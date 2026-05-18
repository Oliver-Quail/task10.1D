package com.task6d.task6d.Model;

import com.google.gson.annotations.SerializedName;

public class LLMResponse {
    @SerializedName("content")
    private String content;
    @SerializedName("prompt")
    private String prompt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
