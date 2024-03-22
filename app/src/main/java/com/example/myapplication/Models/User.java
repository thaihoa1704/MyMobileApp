package com.example.myapplication.Models;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String userType;
    private String address;
    public User(String userName, String email, String phone, String password, String userType, String address) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
        this.address = address;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean validateEmail(){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validatePassword(){
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
