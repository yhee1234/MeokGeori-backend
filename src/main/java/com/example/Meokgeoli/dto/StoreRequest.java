package com.example.Meokgeoli.dto;

public class StoreRequest {
    private String name;
    private String category;
    private double latitude;
    private double longitude;

    public StoreRequest() {}
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}

