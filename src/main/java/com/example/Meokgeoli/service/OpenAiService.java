package com.example.Meokgeoli.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import com.example.Meokgeoli.entity.Store;

@Service
public class OpenAiService {

    private final com.theokanning.openai.service.OpenAiService openAiClient;

    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("OpenAI API key is not configured");
        }
        this.openAiClient = new com.theokanning.openai.service.OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public String getOptimalRoute(List<Store> stores) {
        if (stores == null || stores.isEmpty()) {
            return "방문할 상점이 없습니다.";
        }

        String storeNames = stores.stream()
                .map(Store::getName)
                .collect(Collectors.joining(", "));

        String prompt = String.format(
                "다음 상점들을 방문하려고 해. 가장 효율적인 방문 순서를 추천해줘: %s. 이유는 설명하지 말고, '상점명 -> 상점명 -> ...' 형태로 순서만 알려줘.",
                storeNames
        );

        // ChatMessageRole.USER.value() 대신 직접 문자열 사용
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .maxTokens(100)
                .temperature(0.2)
                .build();

        try {
            return openAiClient.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "경로 최적화 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    public String enhanceProductDescription(String originalDesc) {
        if (originalDesc == null || originalDesc.trim().isEmpty()) {
            return "상품 설명이 없습니다.";
        }

        String prompt = String.format(
                "다음 상품 설명을 SNS에 올릴 것처럼 고객이 사고 싶게끔, 친근하고 매력적인 문장으로 바꿔줘: '%s'. 1~2 문장으로 짧게 요약해줘.",
                originalDesc
        );

        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .maxTokens(200)
                .temperature(0.7)
                .build();

        try {
            return openAiClient.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "상품 설명 개선 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    public String parseUserInput(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "{\"budget\": 50000, \"items\": []}";
        }

        String prompt = String.format(
                "다음 사용자 요청을 분석해서 예산과 핵심 구매 품목을 JSON 형식으로 추출해줘: '%s'. 반드시 다음 형식으로만 답해줘: {\"budget\": 숫자, \"items\": [\"상품1\", \"상품2\"]}",
                userInput
        );

        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .maxTokens(300)
                .temperature(0.3)
                .build();

        try {
            return openAiClient.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "{\"budget\": 50000, \"items\": [], \"error\": \"" + e.getMessage() + "\"}";
        }
    }
}