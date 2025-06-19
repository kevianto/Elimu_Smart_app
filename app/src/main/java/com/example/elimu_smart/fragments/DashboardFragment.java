package com.example.elimu_smart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.elimu_smart.R;
import com.example.elimu_smart.api.PlanAPI;
import com.example.elimu_smart.api.RetrofitClient;
import com.example.elimu_smart.api.TokenManager;
import com.example.elimu_smart.db.DBHelper;
import com.example.elimu_smart.models.PlanResponse;
import com.example.elimu_smart.utils.JwtUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private TextView resultText;

    public DashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        resultText = view.findViewById(R.id.resultText);

        fetchPlanFromBackend();

        return view;
    }

    private void fetchPlanFromBackend() {
        String token = TokenManager.getToken(getActivity());

        if (token == null) {
            Toast.makeText(getActivity(), "Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = JwtUtils.extractUserId(token);
        if (userId == null) {
            Toast.makeText(getActivity(), "Failed to extract user ID from token", Toast.LENGTH_SHORT).show();
            return;
        }

        PlanAPI planAPI = RetrofitClient.getInstance(getActivity()).create(PlanAPI.class);
        Call<PlanResponse> call = planAPI.getPlanByUserId(userId);

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonPlan = new Gson().toJson(response.body().getWeeks());
                    resultText.setText(jsonPlan);

                    new DBHelper(getActivity()).savePlan(jsonPlan); // Save locally
                } else {
                    resultText.setText("No plan found.");
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                resultText.setText("Error fetching plan: " + t.getMessage());
            }
        });
    }
}
