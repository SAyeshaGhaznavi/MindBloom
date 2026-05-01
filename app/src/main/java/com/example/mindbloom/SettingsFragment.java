package com.example.mindbloom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private Switch switchDarkMode;
    private SeekBar seekBarFont;
    private TextView tvFontPreview, tvBack, tvBreadcrumb;
    private RadioGroup rgContrast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply theme preferences early before view is created
        applyPreferences();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchDarkMode  = view.findViewById(R.id.switchDarkMode);
        seekBarFont     = view.findViewById(R.id.seekBarFont);
        tvFontPreview   = view.findViewById(R.id.tvFontPreview);
        tvBack          = view.findViewById(R.id.tvBack);
        tvBreadcrumb    = view.findViewById(R.id.tvBreadcrumb);
        rgContrast      = view.findViewById(R.id.rgContrast);

        SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);

        // 1. Dark Mode Switch
        boolean isDark = prefs.getBoolean("darkMode", false);
        switchDarkMode.setChecked(isDark);

        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean("darkMode", checked).apply();

            // This triggers the UI to recreate and use values-night/colors.xml
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // 2. Font Size
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

        // 3. Contrast
        int contrast = prefs.getInt("contrast", 0);
        if (contrast == 1) rgContrast.check(R.id.rbHighContrast);
        else rgContrast.check(R.id.rbNormalContrast);

        rgContrast.setOnCheckedChangeListener((g, id) ->
                prefs.edit().putInt("contrast", id == R.id.rbHighContrast ? 1 : 0).apply()
        );

        // 4. Back button
        tvBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // 5. FAQ Card
        view.findViewById(R.id.cardFaq).setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), FaqActivity.class))
        );
    }

    private void updateFontPreview(int level) {
        String[] labels = {"Small", "Medium", "Large"};
        float[] sizes   = {13f, 16f, 20f};
        tvFontPreview.setText("Preview: " + labels[level] + " text");
        tvFontPreview.setTextSize(sizes[level]);
    }

    private void applyPreferences() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}