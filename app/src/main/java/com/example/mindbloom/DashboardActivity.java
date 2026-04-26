package com.example.mindbloom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout cardSubjects, cardHomework, cardLectures, cardQuizzes, layoutProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        tvWelcome     = findViewById(R.id.tvWelcome);
        cardSubjects  = findViewById(R.id.cardSubjects);
        cardHomework  = findViewById(R.id.cardHomework);
        cardLectures  = findViewById(R.id.cardLectures);
        cardQuizzes   = findViewById(R.id.cardQuizzes);
        layoutProfile = findViewById(R.id.layoutProfile);
    }

    private void setupClickListeners() {

        // Profile icon → ProfileSettings
        layoutProfile.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ProfileSettingsActivity.class));
        });

        // Quizzes card → QuizActivity (Math as default subject)
        cardQuizzes.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
            intent.putExtra("subject", "Math");
            startActivity(intent);
        });

        // Other cards — coming soon for now
        cardSubjects.setOnClickListener(v -> showComingSoon("Subjects 📚"));
        cardHomework.setOnClickListener(v -> showComingSoon("Homework ✏️"));
        cardLectures.setOnClickListener(v -> showComingSoon("Lectures 🎥"));
    }

    private void showComingSoon(String feature) {
        Toast.makeText(this, feature + " coming soon! 🌸", Toast.LENGTH_SHORT).show();
    }
}
