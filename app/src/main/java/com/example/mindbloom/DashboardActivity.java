package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout cardSubjects, cardHomework, cardLectures, cardQuizzes,
                         layoutProfile, layoutSettings;
    private Button btnViewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initializeViews();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWelcome();
    }

    private void initializeViews() {
        tvWelcome       = findViewById(R.id.tvWelcome);
        cardSubjects    = findViewById(R.id.cardSubjects);
        cardHomework    = findViewById(R.id.cardHomework);
        cardLectures    = findViewById(R.id.cardLectures);
        cardQuizzes     = findViewById(R.id.cardQuizzes);
        layoutProfile   = findViewById(R.id.layoutProfile);
        layoutSettings  = findViewById(R.id.layoutSettings);
        btnViewProgress = findViewById(R.id.btnViewProgress);
        updateWelcome();
    }

    private void updateWelcome() {
        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        String name = prefs.getString("userName", "");
        tvWelcome.setText(name.isEmpty() ? "Let's Learn!" : "Hey " + name + "! 🌸");
    }

    private void setupClickListeners() {
        layoutProfile.setOnClickListener(v ->
            startActivity(new Intent(this, ProfileSettingsActivity.class)));

        layoutSettings.setOnClickListener(v ->
            startActivity(new Intent(this, SettingsActivity.class)));

        btnViewProgress.setOnClickListener(v ->
            startActivity(new Intent(this, ProgressActivity.class)));

        cardSubjects.setOnClickListener(v -> {
            Intent i = new Intent(this, SubjectSelectionActivity.class);
            i.putExtra("mode", "lesson");
            startActivity(i);
        });

        cardHomework.setOnClickListener(v ->
            startActivity(new Intent(this, HomeworkActivity.class)));

        cardLectures.setOnClickListener(v -> {
            Intent i = new Intent(this, SubjectSelectionActivity.class);
            i.putExtra("mode", "lesson");
            startActivity(i);
        });

        cardQuizzes.setOnClickListener(v -> {
            Intent i = new Intent(this, SubjectSelectionActivity.class);
            i.putExtra("mode", "quiz");
            startActivity(i);
        });
    }
}
