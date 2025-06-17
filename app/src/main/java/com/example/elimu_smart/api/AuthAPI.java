package com.example.elimu_smart.api;

import com.example.elimu_smart.models.LoginRequest;
import com.example.elimu_smart.models.LoginResponse;
import com.example.elimu_smart.models.SignupRequest;
import com.example.elimu_smart.models.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/auth/login") // adjust to your Node.js route
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/auth/register")
    Call<SignupResponse> signup(@Body SignupRequest signupRequest);

}
