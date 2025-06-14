
package com.example.elimu_smart.api;


import com.example.elimu_smart.models.PlanRequest;
import com.example.elimu_smart.models.PlanResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlanAPI {
    @POST("plan/generate")
    Call<PlanResponse> generatePlan(@Body PlanRequest request);
}
