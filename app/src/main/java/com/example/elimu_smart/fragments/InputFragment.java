package com.example.elimu_smart.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elimu_smart.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elimu_smart.api.PlanAPI;
import com.example.elimu_smart.api.RetrofitClient;
import com.example.elimu_smart.models.PlanRequest;
import com.example.elimu_smart.models.PlanResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class InputFragment extends Fragment {
    private EditText careerInput, gradeInput, timeInput;
    private TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        careerInput = view.findViewById(R.id.editCareer);
        gradeInput = view.findViewById(R.id.editGrades);
        timeInput = view.findViewById(R.id.editTime);
        Button submitBtn = view.findViewById(R.id.btnSubmit);
        resultText = view.findViewById(R.id.resultText);

        submitBtn.setOnClickListener(v -> sendToBackend());

        return view;
    }

    private void sendToBackend() {
        String career = careerInput.getText().toString().trim();
        String grades = gradeInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();

        if (career.isEmpty() || grades.isEmpty() || time.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        PlanAPI planAPI = RetrofitClient.getInstance().create(PlanAPI.class);
        Call<PlanResponse> call = planAPI.generatePlan(new PlanRequest(career, grades, time));

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultText.setText(response.body().getPlan());
                } else {
                    Toast.makeText(getActivity(), "Failed to get response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
