package com.example.yummytummyclient;

public class ResClientDetails {
    String name ,resturantId,url="";
    float ratings;
    int pincode;
    public ResClientDetails() {

    }
    public ResClientDetails(String name, String resturantId, float ratings,int pincode) {
        this.name = name;
        this.resturantId = resturantId;
        this.ratings = ratings;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResturantId() {
        return resturantId;
    }

    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
}
