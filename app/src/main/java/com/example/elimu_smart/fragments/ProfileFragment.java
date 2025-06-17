package com.example.elimu_smart.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elimu_smart.R;
import com.example.elimu_smart.api.PlanAPI;
import com.example.elimu_smart.api.RetrofitClient;
import com.example.elimu_smart.models.PlanRequest;
import com.example.elimu_smart.models.PlanResponse;
import com.example.elimu_smart.db.DBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private EditText editCareer, editGrades, editTime;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editCareer = view.findViewById(R.id.editCareer);
        editGrades = view.findViewById(R.id.editGrades);
        editTime = view.findViewById(R.id.editTime);
        Button submitButton = view.findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(v -> sendToBackend());

        return view;
    }

    private void sendToBackend() {

        String career = editCareer.getText().toString().trim();
        String grades = editGrades.getText().toString().trim();
        String time = editTime.getText().toString().trim();

        if (TextUtils.isEmpty(career) || TextUtils.isEmpty(grades) || TextUtils.isEmpty(time)) {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        DBHelper dbHelper = new DBHelper(getActivity());
        dbHelper.saveProfile(career, grades, time);

        PlanAPI planAPI = RetrofitClient.getInstance().create(PlanAPI.class);
        Call<PlanResponse> call = planAPI.generatePlan(new PlanRequest(career, grades, time));

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String plan = response.body().getPlan();
                    dbHelper.savePlan(plan);

                    DashboardFragment dashboardFragment = new DashboardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("plan", plan);
                    dashboardFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, dashboardFragment)
                            .addToBackStack(null)
                            .commit();

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
