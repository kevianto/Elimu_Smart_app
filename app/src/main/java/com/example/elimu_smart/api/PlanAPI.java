
package com.example.elimu_smart.api;


import com.example.elimu_smart.models.PlanRequest;
import com.example.elimu_smart.models.PlanResponse;
import com.example.elimu_smart.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlanAPI {
    @POST("/profile") // Adjust this to match your backend route
    Call<ServerResponse> sendProfileOnly(@Body PlanRequest request);

    @GET("/plan/{userId}")
    Call<PlanResponse> getPlanByUserId(@Path("userId") String userId);

}
