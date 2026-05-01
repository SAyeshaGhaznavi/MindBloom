package com.example.mindbloom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private SharedPreferences prefs;

    // Arrays for Spinners
    private final String[] speechSpeeds = {"Slow", "Normal", "Fast"};
    private final String[] voiceTypes = {"Kid-Friendly", "Male", "Female", "Neutral"};
    private final String[] colorBlindModes = {"Off", "Deuteranopia", "Protanopia", "Tritanopia"};
    private final String[] appThemes = {"Default", "Space", "Animals", "Ocean"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);

        // --- 1. HEADER ---
        view.findViewById(R.id.tvBack).setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.cardFaq).setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), FaqActivity.class)));

        // --- 2. APPEARANCE (Existing) ---
        setupSwitch(view, R.id.switchDarkMode, "darkMode", "Dark mode enabled", "Dark mode disabled");
        setupFontSize(view);
        setupRadioGroup(view, R.id.rgContrast, "contrast",
                new int[]{R.id.rbNormalContrast, R.id.rbHighContrast}, new int[]{0, 1});

        // --- 3. AUDIO & SPEECH ---
        setupSwitch(view, R.id.switchReadAloud, "readAloud", "Read Aloud enabled", "Read Aloud disabled");
        setupSpinner(view, R.id.spinnerSpeechSpeed, "speechSpeed", speechSpeeds);
        setupSpinner(view, R.id.spinnerVoiceType, "voiceType", voiceTypes);
        setupSwitch(view, R.id.switchSoundFx, "soundFx", "Sound Effects ON", "Sound Effects OFF");
        setupSwitch(view, R.id.switchBgMusic, "bgMusic", "Background Music ON", "Background Music OFF");

        // --- 4. VISUAL ACCESSIBILITY ---
        setupSpinner(view, R.id.spinnerColorBlind, "colorBlindMode", colorBlindModes);
        setupSwitch(view, R.id.switchReduceMotion, "reduceMotion", "Animations reduced", "Animations enabled");
        setupSwitch(view, R.id.switchFocusHighlight, "focusHighlight", "Focus Highlight ON", "Focus Highlight OFF");
        setupSwitch(view, R.id.switchReadingGuide, "readingGuide", "Reading Guide ON", "Reading Guide OFF");

        // --- 5. COGNITIVE LOAD ---
        setupSwitch(view, R.id.switchDistractionFree, "distractionFree", "Distraction-Free Mode ON", "Distraction-Free Mode OFF");
        setupRadioGroup(view, R.id.rgHintLevel, "hintLevel",
                new int[]{R.id.rbBeginner, R.id.rbGuided, R.id.rbIndependent}, new int[]{0, 1, 2});
        setupSwitch(view, R.id.switchAutoAdvance, "autoAdvance", "Auto-Advance ON", "Auto-Advance OFF");
        setupSwitch(view, R.id.switchSimpleLanguage, "simpleLanguage", "Simple Language ON", "Simple Language OFF");

        // --- 6. INTERACTION TIMING ---
        setupSwitch(view, R.id.switchQuizTimer, "quizTimer", "Quiz Timer ON", "Quiz Timer OFF");
        setupRadioGroup(view, R.id.rgExtraTime, "extraTime",
                new int[]{R.id.rbTimeNone, R.id.rbTime30, R.id.rbTime60}, new int[]{0, 30, 60});
        setupSwitch(view, R.id.switchAutoPause, "autoPause", "Auto-Pause ON", "Auto-Pause OFF");
        setupSwitch(view, R.id.switchDoubleTap, "doubleTap", "Double Tap Confirmation ON", "Double Tap Confirmation OFF");

        // --- 7. LANGUAGE & CULTURAL ---
        setupLanguageSpinner(view);
        setupSwitch(view, R.id.switchBilingual, "bilingual", "Bilingual Mode ON", "Bilingual Mode OFF");
        setupSwitch(view, R.id.switchSimplifiedEnglish, "simplifiedEnglish", "Simplified English ON", "Simplified English OFF");
        setupSwitch(view, R.id.switchRegionalExamples, "regionalExamples", "Regional Examples ON", "Regional Examples OFF");

        // --- 8. KID SAFETY & PARENTS ---
        setupButton(view, R.id.btnParentLock, "🔒 Parent Mode Lock Activated");
        setupRadioGroup(view, R.id.rgContentFilter, "contentFilter",
                new int[]{R.id.rbAge3_5, R.id.rbAge6_8, R.id.rbAge9_12}, new int[]{0, 1, 2});
        setupSeekBar(view, R.id.seekBarScreenTime, "screenTime", "Screen time set to: ", " mins");
        setupButton(view, R.id.btnProgressReports, "Opening Progress Reports...");

        // --- 9. NAVIGATION & USABILITY ---
        setupSwitch(view, R.id.switchLargeTouch, "largeTouch", "Large Touch Mode ON", "Large Touch Mode OFF");
        setupSwitch(view, R.id.switchGestureNav, "gestureNav", "Gesture Navigation ON", "Gesture Navigation OFF");
        setupSwitch(view, R.id.switchHaptic, "hapticFeedback", "Haptic Feedback ON", "Haptic Feedback OFF");
        setupButton(view, R.id.btnUndoAction, "Undo triggered");

        // --- 10. PERSONALIZATION ---
        setupButton(view, R.id.btnAvatar, "Opening Avatar Customization...");
        setupSpinner(view, R.id.spinnerAppTheme, "appTheme", appThemes);
        setupSwitch(view, R.id.switchRewardAnimations, "rewardAnimations", "Reward Animations ON", "Reward Animations OFF");
        setupSwitch(view, R.id.switchDailyGoal, "dailyGoalReminder", "Daily Goal Reminder ON", "Daily Goal Reminder OFF");
    }

    // --- PROTOTYPE HELPER METHODS (Boilerplate reducers) ---

    private void setupSwitch(View view, int id, String prefKey, String onMsg, String offMsg) {
        Switch sw = view.findViewById(id);
        sw.setChecked(prefs.getBoolean(prefKey, false));
        sw.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(prefKey, checked).apply();
            Toast.makeText(requireContext(), checked ? onMsg : offMsg, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSpinner(View view, int id, String prefKey, String[] items) {
        Spinner sp = view.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        String saved = prefs.getString(prefKey, items[0]);
        int pos = Arrays.asList(items).indexOf(saved);
        if (pos == -1) pos = 0;
        sp.setSelection(pos);

        boolean[] firstLoad = {true};
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (firstLoad[0]) { firstLoad[0] = false; return; }
                prefs.edit().putString(prefKey, items[position]).apply();
                Toast.makeText(requireContext(), items[position] + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRadioGroup(View view, int rgId, String prefKey, int[] rbIds, int[] values) {
        RadioGroup rg = view.findViewById(rgId);
        int savedVal = prefs.getInt(prefKey, values[0]);
        for (int i = 0; i < values.length; i++) {
            if (savedVal == values[i]) { rg.check(rbIds[i]); break; }
        }
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < rbIds.length; i++) {
                if (checkedId == rbIds[i]) {
                    prefs.edit().putInt(prefKey, values[i]).apply();
                    break;
                }
            }
        });
    }

    private void setupSeekBar(View view, int id, String prefKey, String toastPrefix, String toastSuffix) {
        SeekBar sb = view.findViewById(id);
        int progress = prefs.getInt(prefKey, 0);
        sb.setProgress(progress);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int p, boolean fromUser) {
                prefs.edit().putInt(prefKey, p).apply();
                if (fromUser) Toast.makeText(requireContext(), toastPrefix + p + toastSuffix, Toast.LENGTH_SHORT).show();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupButton(View view, int id, String toastMsg) {
        view.findViewById(id).setOnClickListener(v -> Toast.makeText(requireContext(), toastMsg, Toast.LENGTH_SHORT).show());
    }

    // --- SPECIFIC LOGIC FOR EXISTING FEATURES ---

    private void setupFontSize(View view) {
        SeekBar seekBarFont = view.findViewById(R.id.seekBarFont);
        TextView tvFontPreview = view.findViewById(R.id.tvFontPreview);
        seekBarFont.setMax(2);
        int fontSize = prefs.getInt("fontSize", 1);
        seekBarFont.setProgress(fontSize);
        updateFontPreview(tvFontPreview, fontSize);

        seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean u) {
                prefs.edit().putInt("fontSize", p).apply();
                updateFontPreview(tvFontPreview, p);
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private void updateFontPreview(TextView tv, int level) {
        String[] labels = {"Small", "Medium", "Large"};
        float[] sizes   = {13f, 16f, 20f};
        tv.setText("Preview: " + labels[level] + " text");
        tv.setTextSize(sizes[level]);
    }

    private void setupLanguageSpinner(View view) {
        String[] languageNames = {"English", "Spanish", "French", "Hindi", "Arabic", "Urdu"};
        String[] languageCodes = {"en", "es", "fr", "hi", "ar", "ur"};
        Spinner spinnerLanguage = view.findViewById(R.id.spinnerLanguage);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        String savedLang = prefs.getString("appLanguage", "en");
        int position = Arrays.asList(languageCodes).indexOf(savedLang);
        if (position == -1) position = 0;
        spinnerLanguage.setSelection(position);

        boolean[] isFirstLoad = {true};
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (isFirstLoad[0]) { isFirstLoad[0] = false; return; }
                String selectedCode = languageCodes[pos];
                prefs.edit().putString("appLanguage", selectedCode).apply();
                Toast.makeText(requireContext(), "Language: " + languageNames[pos], Toast.LENGTH_SHORT).show();
                updateLocale(requireContext(), selectedCode);
                requireActivity().recreate();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressWarnings("deprecation")
    private void updateLocale(Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}