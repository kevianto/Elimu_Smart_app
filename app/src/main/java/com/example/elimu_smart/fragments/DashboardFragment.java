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
import com.example.elimu_smart.models.Day;
import com.example.elimu_smart.models.PlanResponse;
import com.example.elimu_smart.models.Week;
import com.example.elimu_smart.utils.JwtUtils;

import java.util.Map;

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
                if (response.isSuccessful() && response.body() != null && response.body().getWeeks() != null) {
                    Map<String, Week> weeks = response.body().getWeeks();
                    StringBuilder display = new StringBuilder();

                    for (Map.Entry<String, Week> entry : weeks.entrySet()) {
                        String weekKey = entry.getKey();
                        Week week = entry.getValue();

                        display.append("\n\n📘 ").append(weekKey.toUpperCase()).append(" - ").append(week.getTitle()).append("\n");
                        display.append(week.getDescription()).append("\n");

                        appendDay(display, "Day 1", week.getDay1());
                        appendDay(display, "Day 2", week.getDay2());
                        appendDay(display, "Day 3", week.getDay3());
                        appendDay(display, "Day 4", week.getDay4());
                        appendDay(display, "Day 5", week.getDay5());
                        appendDay(display, "Day 6", week.getDay6());
                        appendDay(display, "Day 7", week.getDay7());
                    }

                    resultText.setText(display.toString());
                    new DBHelper(getActivity()).savePlan(display.toString());

                } else {
                    resultText.setText("No plan found or failed to parse.");
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                resultText.setText("Error fetching plan: " + t.getMessage());
            }
        });
    }

    private void appendDay(StringBuilder sb, String dayLabel, Day day) {
        if (day == null) return;

        sb.append("\n🔹 ").append(dayLabel).append("\n");
        sb.append("Task: ").append(day.getTask()).append("\n");
        sb.append("Milestone: ").append(day.getMilestone()).append("\n");
        sb.append("Resources:\n");
        for (String res : day.getResources()) {
            sb.append(" - ").append(res).append("\n");
        }
    }
}
