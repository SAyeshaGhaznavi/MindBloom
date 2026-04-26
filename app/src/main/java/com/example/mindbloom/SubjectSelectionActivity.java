package com.example.mindbloom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SubjectSelectionActivity extends AppCompatActivity {

    // mode = "quiz" or "lesson" — passed from dashboard
    private String mode = "quiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        mode = getIntent().getStringExtra("mode");
        if (mode == null) mode = "quiz";

        TextView tvTitle = findViewById(R.id.tvSubjectTitle);
        tvTitle.setText(mode.equals("lesson") ? "Choose a Subject 📚" : "Pick Your Quiz 🧠");

        LinearLayout cardMath    = findViewById(R.id.cardMath);
        LinearLayout cardEnglish = findViewById(R.id.cardEnglish);
        LinearLayout cardScience = findViewById(R.id.cardScience);
        LinearLayout cardUrdu    = findViewById(R.id.cardUrdu);
        LinearLayout cardHistory = findViewById(R.id.cardHistory);
        LinearLayout cardArt     = findViewById(R.id.cardArt);

        cardMath.setOnClickListener(v    -> launch("Math"));
        cardEnglish.setOnClickListener(v -> launch("English"));
        cardScience.setOnClickListener(v -> launch("Science"));
        cardUrdu.setOnClickListener(v    -> launch("Urdu"));
        cardHistory.setOnClickListener(v -> launch("History"));
        cardArt.setOnClickListener(v     -> launch("Art"));

        findViewById(R.id.btnSubjectBack).setOnClickListener(v -> finish());
    }

    private void launch(String subject) {
        Intent intent;
        if (mode.equals("lesson")) {
            intent = new Intent(this, LessonActivity.class);
        } else {
            intent = new Intent(this, QuizActivity.class);
        }
        intent.putExtra("subject", subject);
        startActivity(intent);
    }
}
