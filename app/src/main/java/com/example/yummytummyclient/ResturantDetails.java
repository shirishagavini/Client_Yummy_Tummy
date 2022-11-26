package com.example.yummytummyclient;

public class ResturantDetails {
    private String name,phone,address;
    private String walletBalance = "0";

    public String getName() {
        return name;
    }

    public ResturantDetails(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public ResturantDetails() {
    }

    @Override
    public String toString() {
        return "ResturantDetails{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", walletBalance='" + walletBalance + '\'' +
                '}';
    }
}
