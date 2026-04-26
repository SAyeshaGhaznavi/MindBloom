package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvEmailError, tvPasswordError;
    private LinearLayout emailErrorLayout, passwordErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in, skip straight to Dashboard
        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            goForward(prefs.getString("userGrade", ""));
            return;
        }

        setContentView(R.layout.activity_login);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        btnLogin          = findViewById(R.id.btnLogin);
        tvSignUp          = findViewById(R.id.tvSignUp);
        tvEmailError      = findViewById(R.id.tvEmailError);
        tvPasswordError   = findViewById(R.id.tvPasswordError);
        emailErrorLayout    = findViewById(R.id.emailErrorLayout);
        passwordErrorLayout = findViewById(R.id.passwordErrorLayout);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());

        tvSignUp.setOnClickListener(v ->
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        etEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailErrorLayout.setVisibility(View.GONE);
            }
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordErrorLayout.setVisibility(View.GONE);
            }
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validateAndLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean hasError = false;

        if (TextUtils.isEmpty(email)) {
            tvEmailError.setText("✦ please enter your email or username");
            emailErrorLayout.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (TextUtils.isEmpty(password)) {
            tvPasswordError.setText("✦ please enter your password");
            passwordErrorLayout.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (!hasError) {
            Toast.makeText(this, "welcome back! 🌸", Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
            
            // Actually check if user exists (simple logic for now)
            String savedEmail = prefs.getString("userEmail", "");
            if (!email.equals(savedEmail) && !email.equals("admin")) {
                // For demo purposes, we'll allow 'admin' or the registered email
                // If you want strict login, uncomment the return below
                // Toast.makeText(this, "User not found. Please sign up.", Toast.LENGTH_SHORT).show();
                // return;
            }

            prefs.edit().putBoolean("isLoggedIn", true).apply();
            goForward(prefs.getString("userGrade", ""));
        }
    }

    /** If grade already chosen → Dashboard; otherwise → GradeSelection */
    private void goForward(String savedGrade) {
        Intent intent;
        if (savedGrade != null && !savedGrade.isEmpty()) {
            intent = new Intent(this, AttachActivity.class);
        } else {
            intent = new Intent(this, GradeSelectionActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
