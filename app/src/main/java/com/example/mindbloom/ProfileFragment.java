package com.example.mindbloom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView tvStudentName, tvGrade;
    private EditText etEmail;
    private Switch switchVoiceControl, switchHighContrast, switchLargeText;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupListeners();
    }

    private void initializeViews(View view) {
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvGrade = view.findViewById(R.id.tvGrade);
        etEmail = view.findViewById(R.id.etEmail);
        switchVoiceControl = view.findViewById(R.id.switchVoiceControl);
        switchHighContrast = view.findViewById(R.id.switchHighContrast);
        switchLargeText = view.findViewById(R.id.switchLargeText);
        btnLogout = view.findViewById(R.id.btnLogout);

        SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);
        String name = prefs.getString("userName", "student name");
        String grade = prefs.getString("userGrade", "grade level");
        String email = prefs.getString("userEmail", "student@email.com");

        tvStudentName.setText(name);
        tvGrade.setText(grade);
        etEmail.setText(email);
    }

    private void setupListeners() {
        switchVoiceControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(requireContext(), "voice control on 🎤", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "voice control off", Toast.LENGTH_SHORT).show();
            }
        });

        switchHighContrast.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(requireContext(), "high contrast on 👁️", Toast.LENGTH_SHORT).show();
            }
        });

        switchLargeText.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(requireContext(), "large text on 🔤", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);
            prefs.edit()
                    .putBoolean("isLoggedIn", false)
                    .putBoolean("onboardingDone", false)
                    .putString("userName", "")
                    .putString("userEmail", "")
                    .putString("userGrade", "")
                    .apply();

            Intent intent = new Intent(requireActivity(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}