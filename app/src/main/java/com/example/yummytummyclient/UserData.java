package com.example.yummytummyclient;

public class UserData {
    String isFormFilled;
    String userId,userType = "client";

    public String getUserType()
    {
        return this.userType;
    }

    public UserData()
    {
    }

    public UserData(String isFormFilled, String userId)
    {
        this.isFormFilled = isFormFilled;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "isFormFilled='" + isFormFilled + '\'' +
                ", UserId='" + this.userId + '\'' +
                ", UserType='" + userType + '\'' +
                '}';
    }

    public String getIsFormFilled() {
        return isFormFilled;
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return this.userId;
    }

}

