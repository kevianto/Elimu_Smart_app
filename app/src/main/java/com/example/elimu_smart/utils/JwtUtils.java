package com.example.elimu_smart.utils;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {
    public static String extractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
            JSONObject json = new JSONObject(payload);

            return json.optString("id"); // Adjust to match your token payload
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

