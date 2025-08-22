package com.example.Meokgeoli.dto.response;

public class StoreLocationDto {
    private double latitude;
    private double longitude;

    public StoreLocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}