package com.example.elimu_smart.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elimu_smart.R;
import com.example.elimu_smart.db.DBHelper;

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

        if (getArguments() != null) {
            String plan = getArguments().getString("plan");
            resultText.setText(plan);

        }
        if (getArguments() != null && getArguments().containsKey("plan")) {
            resultText.setText(getArguments().getString("plan"));
        } else {
            // Load from SQLite
            DBHelper dbHelper = new DBHelper(getActivity());
            String plan = dbHelper.getLatestPlan();
            resultText.setText(plan);
        }


        return view;
    }
}
