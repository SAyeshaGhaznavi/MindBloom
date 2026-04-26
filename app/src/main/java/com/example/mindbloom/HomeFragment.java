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
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private TextView tvWelcome;
    private LinearLayout cardSubjects, cardHomework, cardLectures, cardQuizzes,
            layoutProfile, layoutSettings;
    private Button btnViewProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupClickListeners();
        updateWelcome();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Added null check just to be safe in fragments
        if (tvWelcome != null) {
            updateWelcome();
        }
    }

    private void initializeViews(View view) {
        tvWelcome       = view.findViewById(R.id.tvWelcome);
        cardSubjects    = view.findViewById(R.id.cardSubjects);
        cardHomework    = view.findViewById(R.id.cardHomework);
        cardLectures    = view.findViewById(R.id.cardLectures);
        cardQuizzes     = view.findViewById(R.id.cardQuizzes);
        layoutProfile   = view.findViewById(R.id.layoutProfile);
        layoutSettings  = view.findViewById(R.id.layoutSettings);
        btnViewProgress = view.findViewById(R.id.btnViewProgress);
    }

    private void updateWelcome() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);
        String name = prefs.getString("userName", "");
        tvWelcome.setText(name.isEmpty() ? "Let's Learn!" : "Hey " + name + "! 🌸");
    }

    private void setupClickListeners() {
        layoutProfile.setOnClickListener(v -> {
            if (getActivity() instanceof AttachActivity) {
                ((AttachActivity) requireActivity()).switchTab(3);
            }
        });

        // --- CHANGED: Switch to Settings Tab (Index 2) ---
        layoutSettings.setOnClickListener(v -> {
            if (getActivity() instanceof AttachActivity) {
                ((AttachActivity) requireActivity()).switchTab(2);
            }
        });

        // --- CHANGED: Switch to Progress Tab (Index 1) ---
        btnViewProgress.setOnClickListener(v -> {
            if (getActivity() instanceof AttachActivity) {
                ((AttachActivity) requireActivity()).switchTab(1);
            }
        });

        cardSubjects.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), SubjectSelectionActivity.class);
            i.putExtra("mode", "lesson");
            startActivity(i);
        });

        cardHomework.setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), HomeworkActivity.class)));

        cardLectures.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), SubjectSelectionActivity.class);
            i.putExtra("mode", "lesson");
            startActivity(i);
        });

        cardQuizzes.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), SubjectSelectionActivity.class);
            i.putExtra("mode", "quiz");
            startActivity(i);
        });
    }
}