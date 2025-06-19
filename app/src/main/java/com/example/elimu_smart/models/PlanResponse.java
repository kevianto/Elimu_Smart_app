package com.example.elimu_smart.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PlanResponse {
    @SerializedName("weeks")
    private Map<String, Object> weeks;

    public Map<String, Object> getWeeks() {
        return weeks;
    }
}

