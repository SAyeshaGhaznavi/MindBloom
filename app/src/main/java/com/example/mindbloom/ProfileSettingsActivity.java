package com.example.mindbloom;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView tvStudentName, tvGrade;
    private EditText etEmail;
    private Switch switchVoiceControl, switchHighContrast, switchLargeText;

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

        tvStudentName.setText("alex johnson");
        tvGrade.setText("high school · grades 9-12");
        etEmail.setText("alex@email.com");
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
    }
}