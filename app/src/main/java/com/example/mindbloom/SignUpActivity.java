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

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignUp;
    private LinearLayout nameErrorLayout, emailErrorLayout, passwordErrorLayout;
    private TextView tvNameError, tvEmailError, tvPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Error layouts and text views
        nameErrorLayout = findViewById(R.id.nameErrorLayout);
        emailErrorLayout = findViewById(R.id.emailErrorLayout);
        passwordErrorLayout = findViewById(R.id.passwordErrorLayout);
        tvNameError = findViewById(R.id.tvNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
    }

    private void setupListeners() {
        btnSignUp.setOnClickListener(v -> validateAndSignUp());

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameErrorLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    private void validateAndSignUp() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        boolean hasError = false;

        if (TextUtils.isEmpty(name)) {
            tvNameError.setText("✦ please enter your full name");
            nameErrorLayout.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (TextUtils.isEmpty(email)) {
            tvEmailError.setText("✦ please enter your email or username");
            emailErrorLayout.setVisibility(View.VISIBLE);
            hasError = true;
        }

        String passwordError = getPasswordError(password);
        if (passwordError != null) {
            tvPasswordError.setText("✦ " + passwordError);
            passwordErrorLayout.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (!hasError) {
            Toast.makeText(this, "welcome to MindBloom! 🌸", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, GradeSelectionActivity.class);
            intent.putExtra("userName", name);
            startActivity(intent);
        }
    }

    private String getPasswordError(String password) {
        if (TextUtils.isEmpty(password)) {
            return "password is required";
        }

        if (password.length() < 8) {
            return "password must be at least 8 characters long";
        }

        if (password.length() > 20) {
            return "password must not exceed 20 characters";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "password must contain at least one uppercase letter";
        }

        if (!password.matches(".*[a-z].*")) {
            return "password must contain at least one lowercase letter";
        }

        if (!password.matches(".*\\d.*")) {
            return "password must contain at least one digit";
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "password must contain a special character (!@#$%^&*)";
        }

        return null;
    }
}