package com.example.Meokgeoli.dto.request;

public class RecommendationReqDto {
    private String prompt;
    private UserLocation userLocation;

    public RecommendationReqDto() {}

    // Getter
    public String getPrompt() { return prompt; }
    public UserLocation getUserLocation() { return userLocation; }

    // Setter
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setUserLocation(UserLocation userLocation) { this.userLocation = userLocation; }
}