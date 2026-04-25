package com.example.mindbloom;

import android.content.Intent;
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
        setContentView(R.layout.activity_login);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        emailErrorLayout = findViewById(R.id.emailErrorLayout);
        passwordErrorLayout = findViewById(R.id.passwordErrorLayout);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailErrorLayout.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordErrorLayout.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
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
            Intent intent = new Intent(LoginActivity.this, GradeSelectionActivity.class);
            startActivity(intent);
            finish();
        }
    }
}