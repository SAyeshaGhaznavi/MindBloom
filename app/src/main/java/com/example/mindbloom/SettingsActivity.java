package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private SeekBar seekBarFont;
    private TextView tvFontPreview, tvBack, tvBreadcrumb;
    private RadioGroup rgContrast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPreferences();
        setContentView(R.layout.activity_settings);

        switchDarkMode  = findViewById(R.id.switchDarkMode);
        seekBarFont     = findViewById(R.id.seekBarFont);
        tvFontPreview   = findViewById(R.id.tvFontPreview);
        tvBack          = findViewById(R.id.tvBack);
        tvBreadcrumb    = findViewById(R.id.tvBreadcrumb);
        rgContrast      = findViewById(R.id.rgContrast);

        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);

        // Dark mode
        boolean isDark = prefs.getBoolean("darkMode", false);
        switchDarkMode.setChecked(isDark);
        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("darkMode", checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Font size (0=Small, 1=Medium, 2=Large)
        int fontSize = prefs.getInt("fontSize", 1);
        seekBarFont.setMax(2);
        seekBarFont.setProgress(fontSize);
        updateFontPreview(fontSize);
        seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean u) {
                prefs.edit().putInt("fontSize", p).apply();
                updateFontPreview(p);
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });

        // High contrast
        int contrast = prefs.getInt("contrast", 0);
        if (contrast == 1) rgContrast.check(R.id.rbHighContrast);
        else rgContrast.check(R.id.rbNormalContrast);
        rgContrast.setOnCheckedChangeListener((g, id) ->
            prefs.edit().putInt("contrast", id == R.id.rbHighContrast ? 1 : 0).apply());

        // Breadcrumb
        tvBreadcrumb.setText("Home > Settings");

        // Back
        tvBack.setOnClickListener(v -> finish());

        // FAQ card
        findViewById(R.id.cardFaq).setOnClickListener(v ->
            startActivity(new Intent(this, FaqActivity.class)));
    }

    private void updateFontPreview(int level) {
        String[] labels = {"Small", "Medium", "Large"};
        float[] sizes   = {13f, 16f, 20f};
        tvFontPreview.setText("Preview: " + labels[level] + " text");
        tvFontPreview.setTextSize(sizes[level]);
    }

    private void applyPreferences() {
        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(
            isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
