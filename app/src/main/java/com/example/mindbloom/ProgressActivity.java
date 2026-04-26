package com.example.mindbloom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);

        int totalPoints   = prefs.getInt("totalPoints", 0);
        int quizzesDone   = prefs.getInt("quizzesDone", 0);
        int streak        = prefs.getInt("streak", 0);

        // Total points
        TextView tvPoints = findViewById(R.id.tvTotalPoints);
        tvPoints.setText("⭐ " + totalPoints + " pts");

        // Quizzes done
        TextView tvQuizCount = findViewById(R.id.tvQuizCount);
        tvQuizCount.setText(quizzesDone + " quizzes");

        // Streak
        TextView tvStreak = findViewById(R.id.tvStreak);
        tvStreak.setText("🔥 " + streak + " day streak");

        // Per-subject progress bars
        updateSubjectBar(prefs, "Math",    R.id.barMath,    R.id.tvMathScore);
        updateSubjectBar(prefs, "English", R.id.barEnglish, R.id.tvEnglishScore);
        updateSubjectBar(prefs, "Science", R.id.barScience, R.id.tvScienceScore);
        updateSubjectBar(prefs, "Urdu",    R.id.barUrdu,    R.id.tvUrduScore);
        updateSubjectBar(prefs, "History", R.id.barHistory, R.id.tvHistoryScore);
        updateSubjectBar(prefs, "Art",     R.id.barArt,     R.id.tvArtScore);

        // Badges
        updateBadge(prefs, R.id.badgeFirstQuiz,  R.id.tvBadgeFirstQuiz,  quizzesDone >= 1);
        updateBadge(prefs, R.id.badgeStreak3,    R.id.tvBadgeStreak3,    streak >= 3);
        updateBadge(prefs, R.id.badgeStreak7,    R.id.tvBadgeStreak7,    streak >= 7);
        updateBadge(prefs, R.id.badge100Points,  R.id.tvBadge100Points,  totalPoints >= 100);
        updateBadge(prefs, R.id.badge500Points,  R.id.tvBadge500Points,  totalPoints >= 500);
        updateBadge(prefs, R.id.badgeAllSubjects,R.id.tvBadgeAllSubjects,
                prefs.getInt("best_Math",0) > 0 && prefs.getInt("best_English",0) > 0
                && prefs.getInt("best_Science",0) > 0);

        // Level calculation
        TextView tvLevel = findViewById(R.id.tvLevel);
        String level;
        if      (totalPoints < 50)  level = "🌱 Seedling";
        else if (totalPoints < 150) level = "🌿 Sprout";
        else if (totalPoints < 300) level = "🌸 Blossom";
        else if (totalPoints < 500) level = "🌳 Tree";
        else                         level = "🌺 MindBloom Master";
        tvLevel.setText(level);

        // Progress bar for level
        ProgressBar pbLevel = findViewById(R.id.pbLevel);
        int nextMilestone = totalPoints < 50 ? 50 : totalPoints < 150 ? 150 :
                totalPoints < 300 ? 300 : totalPoints < 500 ? 500 : 500;
        pbLevel.setMax(nextMilestone);
        pbLevel.setProgress(Math.min(totalPoints, nextMilestone));

        findViewById(R.id.btnProgressBack).setOnClickListener(v -> finish());
    }

    private void updateSubjectBar(SharedPreferences prefs, String subject,
                                   int barId, int textId) {
        int best = prefs.getInt("best_" + subject, 0); // 0-50 (5 questions × 10 pts)
        ProgressBar bar = findViewById(barId);
        TextView tv = findViewById(textId);
        bar.setMax(50);
        bar.setProgress(best);
        int pct = best * 2; // 50 pts = 100%
        tv.setText(pct + "%");
    }

    private void updateBadge(SharedPreferences prefs, int emojiViewId,
                              int textViewId, boolean earned) {
        TextView emoji = findViewById(emojiViewId);
        TextView label = findViewById(textViewId);
        if (earned) {
            emoji.setAlpha(1f);
            label.setAlpha(1f);
        } else {
            emoji.setAlpha(0.3f);
            label.setAlpha(0.3f);
        }
    }
}
