package com.example.mindbloom;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("MindBloomPrefs", Context.MODE_PRIVATE);

        int totalPoints   = prefs.getInt("totalPoints", 0);
        int quizzesDone   = prefs.getInt("quizzesDone", 0);
        int streak        = prefs.getInt("streak", 0);

        // Total points
        TextView tvPoints = view.findViewById(R.id.tvTotalPoints);
        tvPoints.setText("⭐ " + totalPoints + " pts");

        // Quizzes done
        TextView tvQuizCount = view.findViewById(R.id.tvQuizCount);
        tvQuizCount.setText(quizzesDone + " quizzes");

        // Streak
        TextView tvStreak = view.findViewById(R.id.tvStreak);
        tvStreak.setText("🔥 " + streak + " day streak");

        // Per-subject progress bars
        updateSubjectBar(prefs, view, "Math",    R.id.barMath,    R.id.tvMathScore);
        updateSubjectBar(prefs, view, "English", R.id.barEnglish, R.id.tvEnglishScore);
        updateSubjectBar(prefs, view, "Science", R.id.barScience, R.id.tvScienceScore);
        updateSubjectBar(prefs, view, "Urdu",    R.id.barUrdu,    R.id.tvUrduScore);
        updateSubjectBar(prefs, view, "History", R.id.barHistory, R.id.tvHistoryScore);
        updateSubjectBar(prefs, view, "Art",     R.id.barArt,     R.id.tvArtScore);

        // Badges
        updateBadge(prefs, view, R.id.badgeFirstQuiz,  R.id.tvBadgeFirstQuiz,  quizzesDone >= 1);
        updateBadge(prefs, view, R.id.badgeStreak3,    R.id.tvBadgeStreak3,    streak >= 3);
        updateBadge(prefs, view, R.id.badgeStreak7,    R.id.tvBadgeStreak7,    streak >= 7);
        updateBadge(prefs, view, R.id.badge100Points,  R.id.tvBadge100Points,  totalPoints >= 100);
        updateBadge(prefs, view, R.id.badge500Points,  R.id.tvBadge500Points,  totalPoints >= 500);
        updateBadge(prefs, view, R.id.badgeAllSubjects,R.id.tvBadgeAllSubjects,
                prefs.getInt("best_Math",0) > 0 && prefs.getInt("best_English",0) > 0
                        && prefs.getInt("best_Science",0) > 0);

        // Level calculation
        TextView tvLevel = view.findViewById(R.id.tvLevel);
        String level;
        if      (totalPoints < 50)  level = "🌱 Seedling";
        else if (totalPoints < 150) level = "🌿 Sprout";
        else if (totalPoints < 300) level = "🌸 Blossom";
        else if (totalPoints < 500) level = "🌳 Tree";
        else                         level = "🌺 MindBloom Master";
        tvLevel.setText(level);

        // Progress bar for level
        ProgressBar pbLevel = view.findViewById(R.id.pbLevel);
        int nextMilestone = totalPoints < 50 ? 50 : totalPoints < 150 ? 150 :
                totalPoints < 300 ? 300 : totalPoints < 500 ? 500 : 500;
        pbLevel.setMax(nextMilestone);
        pbLevel.setProgress(Math.min(totalPoints, nextMilestone));

        // Since this is now inside a ViewPager2, "finish()" won't work.
        // We use onBackPressed() to close the AttachActivity instead.
        view.findViewById(R.id.btnProgressBack).setOnClickListener(v ->
                requireActivity().onBackPressed()
        );
    }

    private void updateSubjectBar(SharedPreferences prefs, View view, String subject,
                                  int barId, int textId) {
        int best = prefs.getInt("best_" + subject, 0); // 0-50 (5 questions × 10 pts)
        ProgressBar bar = view.findViewById(barId);
        TextView tv = view.findViewById(textId);
        bar.setMax(50);
        bar.setProgress(best);
        int pct = best * 2; // 50 pts = 100%
        tv.setText(pct + "%");
    }

    private void updateBadge(SharedPreferences prefs, View view, int emojiViewId,
                             int textViewId, boolean earned) {
        TextView emoji = view.findViewById(emojiViewId);
        TextView label = view.findViewById(textViewId);
        if (earned) {
            emoji.setAlpha(1f);
            label.setAlpha(1f);
        } else {
            emoji.setAlpha(0.3f);
            label.setAlpha(0.3f);
        }
    }
}