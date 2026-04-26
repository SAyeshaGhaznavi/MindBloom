package com.example.mindbloom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class HomeworkActivity extends AppCompatActivity {

    private LinearLayout homeworkContainer;
    private SharedPreferences prefs;

    // Homework items: {title, subject, due, points}
    private static final String[][] HOMEWORK = {
        {"Multiplication Table (×7 and ×8)", "Math", "Tomorrow", "20"},
        {"Write 5 sentences using adjectives", "English", "In 2 days", "15"},
        {"Draw the water cycle diagram", "Science", "This week", "25"},
        {"Read pages 10–15 of Urdu book", "Urdu", "Tomorrow", "10"},
        {"Solve 10 addition problems", "Math", "Today", "15"},
        {"Write a short paragraph about your hero", "English", "In 3 days", "20"},
        {"List 5 facts about the Solar System", "Science", "This week", "15"},
        {"Write your name in Urdu 3 times", "Urdu", "Today", "10"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        homeworkContainer = findViewById(R.id.homeworkContainer);

        buildHomeworkList();
        updatePointsDisplay();

        findViewById(R.id.btnHomeworkBack).setOnClickListener(v -> finish());
    }

    private void buildHomeworkList() {
        homeworkContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < HOMEWORK.length; i++) {
            final int idx = i;
            String[] hw = HOMEWORK[i];
            boolean done = prefs.getBoolean("hw_done_" + i, false);

            View item = inflater.inflate(R.layout.item_homework, homeworkContainer, false);

            CheckBox cb    = item.findViewById(R.id.cbHomework);
            TextView title = item.findViewById(R.id.tvHomeworkTitle);
            TextView sub   = item.findViewById(R.id.tvHomeworkSubject);
            TextView due   = item.findViewById(R.id.tvHomeworkDue);
            TextView pts   = item.findViewById(R.id.tvHomeworkPoints);

            title.setText(hw[0]);
            sub.setText(hw[1]);
            due.setText("Due: " + hw[2]);
            pts.setText("+" + hw[3] + " pts");

            cb.setChecked(done);
            if (done) {
                title.setPaintFlags(title.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                title.setAlpha(0.5f);
                item.setAlpha(0.6f);
            }

            cb.setOnCheckedChangeListener((btn, isChecked) -> {
                boolean wasDone = prefs.getBoolean("hw_done_" + idx, false);
                if (isChecked && !wasDone) {
                    int pts2 = Integer.parseInt(HOMEWORK[idx][3]);
                    int existing = prefs.getInt("totalPoints", 0);
                    int hwCount  = prefs.getInt("quizzesDone", 0);
                    prefs.edit()
                         .putBoolean("hw_done_" + idx, true)
                         .putInt("totalPoints", existing + pts2)
                         .putInt("quizzesDone", hwCount + 1)
                         .apply();
                    Toast.makeText(this, "✅ Great job! +" + pts2 + " points!", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    int pts2 = Integer.parseInt(HOMEWORK[idx][3]);
                    int existing = prefs.getInt("totalPoints", 0);
                    prefs.edit()
                         .putBoolean("hw_done_" + idx, false)
                         .putInt("totalPoints", Math.max(0, existing - pts2))
                         .apply();
                }
                updatePointsDisplay();
                buildHomeworkList();
            });

            homeworkContainer.addView(item);
        }
    }

    private void updatePointsDisplay() {
        int total = prefs.getInt("totalPoints", 0);
        TextView tvPoints = findViewById(R.id.tvHomeworkPoints);
        if (tvPoints != null) tvPoints.setText("⭐ " + total + " pts");

        // Count completed
        int done = 0;
        for (int i = 0; i < HOMEWORK.length; i++) {
            if (prefs.getBoolean("hw_done_" + i, false)) done++;
        }
        TextView tvProgress = findViewById(R.id.tvHomeworkProgress);
        if (tvProgress != null) tvProgress.setText(done + " / " + HOMEWORK.length + " completed");

        ProgressBar pb = findViewById(R.id.pbHomework);
        if (pb != null) { pb.setMax(HOMEWORK.length); pb.setProgress(done); }
    }
}
