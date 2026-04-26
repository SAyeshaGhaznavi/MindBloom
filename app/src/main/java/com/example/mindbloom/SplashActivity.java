package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appName = findViewById(R.id.appName);
        TextView tagline = findViewById(R.id.tagline);

        appName.animate().alpha(1f).setDuration(1200)
            .withEndAction(() -> tagline.animate().alpha(1f).setDuration(800).start())
            .start();

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
            boolean onboardingDone = prefs.getBoolean("onboardingDone", false);
            boolean isLoggedIn     = prefs.getBoolean("isLoggedIn", false);
            String userName        = prefs.getString("userName", "");

            Intent intent;
            if (!onboardingDone) {
                // 1. First time? Show Onboarding
                intent = new Intent(this, OnboardingActivity.class);
            } else if (!isLoggedIn) {
                // 2. Not logged in?
                if (userName.isEmpty()) {
                    // Never signed up? Go to SignUp
                    intent = new Intent(this, SignUpActivity.class);
                } else {
                    // Registered but logged out? Go to Login
                    intent = new Intent(this, LoginActivity.class);
                }
            } else {
                // 3. Logged in? Go to Dashboard
                intent = new Intent(this, AttachActivity.class);
            }

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2500);
    }
}
