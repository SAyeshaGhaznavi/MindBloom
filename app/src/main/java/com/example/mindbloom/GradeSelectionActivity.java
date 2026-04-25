package com.example.mindbloom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GradeSelectionActivity extends AppCompatActivity {

    private LinearLayout layoutPrimary, layoutMiddle, layoutHigh, layoutUniversity;
    private Button btnContinue;
    private String selectedGrade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_selection);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        layoutPrimary = findViewById(R.id.layoutPrimary);
        layoutMiddle = findViewById(R.id.layoutMiddle);
        layoutHigh = findViewById(R.id.layoutHigh);
        layoutUniversity = findViewById(R.id.layoutUniversity);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void setupListeners() {
        layoutPrimary.setOnClickListener(v -> {
            resetAllSelections();
            layoutPrimary.setBackgroundResource(R.drawable.grade_option_selected_bg);
            selectedGrade = "Primary School";
        });

        layoutMiddle.setOnClickListener(v -> {
            resetAllSelections();
            layoutMiddle.setBackgroundResource(R.drawable.grade_option_selected_bg);
            selectedGrade = "Middle School";
        });

        layoutHigh.setOnClickListener(v -> {
            resetAllSelections();
            layoutHigh.setBackgroundResource(R.drawable.grade_option_selected_bg);
            selectedGrade = "High School";
        });

        layoutUniversity.setOnClickListener(v -> {
            resetAllSelections();
            layoutUniversity.setBackgroundResource(R.drawable.grade_option_selected_bg);
            selectedGrade = "University / College";
        });

        btnContinue.setOnClickListener(v -> {
            if (selectedGrade.isEmpty()) {
                Toast.makeText(this, "please select your grade ✨", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(GradeSelectionActivity.this, DashboardActivity.class);
            intent.putExtra("grade", selectedGrade);
            startActivity(intent);
        });
    }

    private void resetAllSelections() {
        layoutPrimary.setBackgroundResource(R.drawable.grade_option_bg);
        layoutMiddle.setBackgroundResource(R.drawable.grade_option_bg);
        layoutHigh.setBackgroundResource(R.drawable.grade_option_bg);
        layoutUniversity.setBackgroundResource(R.drawable.grade_option_bg);
    }
}