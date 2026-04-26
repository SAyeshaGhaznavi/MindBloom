package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private final String[] titles = {
        "Welcome to MindBloom 🌸",
        "Learn at Your Pace 📚",
        "Quiz & Earn Points 🏆",
        "Track Your Progress 📊",
        "You're All Set! 🎉"
    };

    private final String[] descriptions = {
        "MindBloom helps you learn, grow, and bloom! Let's take a quick tour to get you started.",
        "Browse subjects like Math, Science, English and more. Read lessons before taking a quiz.",
        "Answer quiz questions correctly to earn points and unlock badges. Every point counts!",
        "Check your homework tasks and view your progress to see how far you've come.",
        "Tap any card on the dashboard to start learning. You can revisit this guide in Settings anytime!"
    };

    private final String[] emojis = {"🌸", "📚", "🏆", "📊", "🎉"};

    private int currentPage = 0;
    private TextView tvTitle, tvDesc, tvEmoji, tvSkip, tvStep;
    private Button btnNext;
    private LinearLayout dotsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        tvTitle      = findViewById(R.id.tvOnboardTitle);
        tvDesc       = findViewById(R.id.tvOnboardDesc);
        tvEmoji      = findViewById(R.id.tvOnboardEmoji);
        tvSkip       = findViewById(R.id.tvSkip);
        tvStep       = findViewById(R.id.tvStep);
        btnNext      = findViewById(R.id.btnNext);
        dotsContainer = findViewById(R.id.dotsContainer);

        buildDots();
        showPage(0);

        btnNext.setOnClickListener(v -> {
            if (currentPage < titles.length - 1) {
                showPage(++currentPage);
            } else {
                finishOnboarding();
            }
        });

        tvSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void showPage(int index) {
        currentPage = index;
        tvTitle.setText(titles[index]);
        tvDesc.setText(descriptions[index]);
        tvEmoji.setText(emojis[index]);
        tvStep.setText((index + 1) + " of " + titles.length);
        btnNext.setText(index == titles.length - 1 ? "Get Started!" : "Next →");
        updateDots(index);
    }

    private void buildDots() {
        dotsContainer.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(12f);
            dot.setPadding(6, 0, 6, 0);
            dotsContainer.addView(dot);
        }
        updateDots(0);
    }

    private void updateDots(int active) {
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            TextView dot = (TextView) dotsContainer.getChildAt(i);
            dot.setTextColor(i == active ? 0xFF7C6FF7 : 0xFFCCCCCC);
        }
    }

    private void finishOnboarding() {
        getSharedPreferences("MindBloomPrefs", MODE_PRIVATE)
            .edit().putBoolean("onboardingDone", true).apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
