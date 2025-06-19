package com.example.elimu_smart.models;

public class ServerResponse {
    private String message; // e.g., "Profile saved", or "Success"

    public ServerResponse() {}

    public ServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

