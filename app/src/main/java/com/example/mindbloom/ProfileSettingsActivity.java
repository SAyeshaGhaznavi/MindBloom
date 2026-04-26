package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView tvStudentName, tvGrade;
    private EditText etEmail;
    private Switch switchVoiceControl, switchHighContrast, switchLargeText;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        tvStudentName = findViewById(R.id.tvStudentName);
        tvGrade = findViewById(R.id.tvGrade);
        etEmail = findViewById(R.id.etEmail);
        switchVoiceControl = findViewById(R.id.switchVoiceControl);
        switchHighContrast = findViewById(R.id.switchHighContrast);
        switchLargeText = findViewById(R.id.switchLargeText);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
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
                Toast.makeText(this, "voice control on 🎤", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "voice control off", Toast.LENGTH_SHORT).show();
            }
        });

        switchHighContrast.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "high contrast on 👁️", Toast.LENGTH_SHORT).show();
            }
        });

        switchLargeText.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "large text on 🔤", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
            prefs.edit()
                .putBoolean("isLoggedIn", false)
                .putBoolean("onboardingDone", false)
                .putString("userName", "")
                .putString("userEmail", "")
                .putString("userGrade", "")
                .apply();

            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
