package com.example.Meokgeoli.dto.request;

public class UserLocation {
    private double latitude;
    private double longitude;

    public UserLocation() {}

    // Getter
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setter
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}