package com.example.Meokgeoli.dto;

import com.example.Meokgeoli.entity.Store;

public class StoreResponse {
    private Long id;
    private String name;
    private String category;
    private double latitude;
    private double longitude;

    public StoreResponse(Long id, String name, String category, double latitude, double longitude) {
        this.id = id; this.name = name; this.category = category;
        this.latitude = latitude; this.longitude = longitude;
    }

    public static StoreResponse from(Store s) {
        return new StoreResponse(s.getId(), s.getName(), s.getCategory(), s.getLatitude(), s.getLongitude());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}

