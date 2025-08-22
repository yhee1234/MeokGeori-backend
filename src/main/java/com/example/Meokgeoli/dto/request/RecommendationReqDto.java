package com.example.Meokgeoli.dto.request;

public class RecommendationReqDto {
    private String prompt;
    private UserLocation userLocation;

    // Getter
    public String getPrompt() { return prompt; }
    public UserLocation getUserLocation() { return userLocation; }
}