package com.example.Meokgeoli.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
// import com.theokanning.openai.service.OpenAiService; // 이 줄은 아래에서 이름이 겹치므로, 직접 전체 경로를 사용합니다.
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import com.example.Meokgeoli.entity.Store;

@Service
// 1. 클래스 이름을 파일명과 동일하게 'OpenAiService'로 수정합니다.
public class OpenAiService {

    // 2. 이 변수의 타입은 외부 라이브러리의 클래스 이름이므로 그대로 둡니다.
    //    우리 클래스 이름과 겹치기 때문에, 혼동을 막기 위해 전체 경로를 명시해주는 것이 가장 좋습니다.
    private final com.theokanning.openai.service.OpenAiService openAiService;

    // 3. 생성자 이름도 클래스 이름과 동일하게 수정합니다.
    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        // 외부 라이브러리의 OpenAiService 객체를 생성하는 부분
        this.openAiService = new com.theokanning.openai.service.OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    // (이하 메소드들은 그대로 유지)
    public String getOptimalRoute(List<Store> stores) {
        String storeNames = stores.stream()
                .map(Store::getName)
                .collect(Collectors.joining(", "));

        String prompt = String.format(
                "다음 상점들을 방문하려고 해. 가장 효율적인 방문 순서를 추천해줘: %s. 이유는 설명하지 말고, '상점명 -> 상점명 -> ...' 형태로 순서만 알려줘.",
                storeNames
        );

        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .maxTokens(100)
                .temperature(0.2)
                .build();

        return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }

    public String enhanceProductDescription(String originalDesc) {
        String prompt = String.format(
                "다음 상품 설명을 SNS에 올릴 것처럼 고객이 사고 싶게끔, 친근하고 매력적인 문장으로 바꿔줘: '%s'. 1~2 문장으로 짧게 요약해줘.",
                originalDesc
        );
        ChatMessage userMessage = new ChatMessage("user", prompt);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .maxTokens(200)
                .build();
        return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }

    public String parseUserInput(String userInput) {
        String prompt = String.format(
                "다음 사용자 요청을 분석해서 예산과 핵심 구매 품목을 JSON 형식으로 추출해줘: '%s'. 예시는 다음과 같아. {\"budget\": 10000, \"items\": [\"삼겹살\", \"상추\", \"음료수\"]}",
                userInput
        );
        ChatMessage userMessage = new ChatMessage("user", prompt);
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(userMessage))
                .build();
        return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }
}