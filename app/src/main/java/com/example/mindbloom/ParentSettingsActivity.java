package com.example.mindbloom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParentSettingsActivity extends AppCompatActivity {

    private LinearLayout layoutPasscode, itemFamilyProfile, itemScreenTime, itemFilterContent, itemReports, itemPrivacy;
    private Button btnSaveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_settings);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        layoutPasscode   = findViewById(R.id.layoutPasscode);
        itemFamilyProfile = findViewById(R.id.itemFamilyProfile);
        itemScreenTime    = findViewById(R.id.itemScreenTime);
        itemFilterContent = findViewById(R.id.itemFilterContent);
        itemReports       = findViewById(R.id.itemReports);
        itemPrivacy       = findViewById(R.id.itemPrivacy);
        btnSaveConfig     = findViewById(R.id.btnSaveConfig);
    }

    private void setupListeners() {
        layoutPasscode.setOnClickListener(v ->
                Toast.makeText(this, "Open Passcode Dialog � keypad", Toast.LENGTH_SHORT).show()
        );

        itemFamilyProfile.setOnClickListener(v ->
                Toast.makeText(this, "Open Family Profiles", Toast.LENGTH_SHORT).show()
        );

        itemScreenTime.setOnClickListener(v ->
                Toast.makeText(this, "Open Screen Time Scheduler", Toast.LENGTH_SHORT).show()
        );

        itemFilterContent.setOnClickListener(v ->
                Toast.makeText(this, "Open Content Filters", Toast.LENGTH_SHORT).show()
        );

        itemReports.setOnClickListener(v ->
                Toast.makeText(this, "Open Activity Reports", Toast.LENGTH_SHORT).show()
        );

        itemPrivacy.setOnClickListener(v ->
                Toast.makeText(this, "Open Privacy Settings", Toast.LENGTH_SHORT).show()
        );

        btnSaveConfig.setOnClickListener(v -> {
            Toast.makeText(this, "Configuration Saved ✅", Toast.LENGTH_SHORT).show();
            finish(); // Go back to Profile screen
        });
    }

    // Allow physical back button to return to profile
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}