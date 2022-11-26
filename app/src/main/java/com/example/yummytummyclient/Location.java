package com.example.yummytummyclient;

public class Location {
    String userId,userName;
    double longitude,latitude;


    public Location() {
    }

    public Location(String userId, String userName, double longitude, double latitude) {
        this.userId = userId;
        this.userName = userName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
