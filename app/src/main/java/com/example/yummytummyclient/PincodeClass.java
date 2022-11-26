package com.example.yummytummyclient;

public class PincodeClass {
    public PincodeClass() {
    }
    String pincode;

    public PincodeClass(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "PincodeClass{" +
                "pincode='" + pincode + '\'' +
                '}';
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
