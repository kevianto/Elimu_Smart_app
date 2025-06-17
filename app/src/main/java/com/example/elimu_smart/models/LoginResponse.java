package com.example.elimu_smart.models;

public class LoginResponse {
    private String message;
    private String token; // optional if you return token

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}

