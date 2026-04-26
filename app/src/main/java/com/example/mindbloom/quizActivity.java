package com.example.mindbloom;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvQuestionCount;
    private ProgressBar progressBar;
    private Button[] optionButtons;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false; // stops double-tapping

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get subject name sent from HomeActivity
        String subject = getIntent().getStringExtra("subject");
        if (subject == null) subject = "General";

        // Connect all views
        tvQuestion      = findViewById(R.id.tvQuestion);
        tvScore         = findViewById(R.id.tvScore);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        progressBar     = findViewById(R.id.progressBar);

        optionButtons = new Button[]{
                findViewById(R.id.btnOption1),
                findViewById(R.id.btnOption2),
                findViewById(R.id.btnOption3),
                findViewById(R.id.btnOption4)
        };

        // Load questions based on subject
        questions = getQuestionsForSubject(subject);
        progressBar.setMax(questions.size());

        // Show the first question
        loadQuestion();

        // Each button checks if that option (0-3) is correct
        for (int i = 0; i < 4; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> checkAnswer(index));
        }

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            showResults();
            return;
        }

        answered = false;
        Question q = questions.get(currentIndex);

        tvQuestion.setText(q.getQuestionText());
        tvQuestionCount.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        tvScore.setText("⭐ " + score);
        progressBar.setProgress(currentIndex + 1);

        // Reset all buttons to white
        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(opts[i]);
            optionButtons[i].setBackgroundColor(Color.WHITE);
            optionButtons[i].setTextColor(Color.parseColor("#333333"));
            optionButtons[i].setEnabled(true);
        }
    }

    private void checkAnswer(int selectedIndex) {
        if (answered) return; // ignore taps after first one
        answered = true;

        // Disable all buttons immediately
        for (Button btn : optionButtons) btn.setEnabled(false);

        int correct = questions.get(currentIndex).getCorrectAnswerIndex();

        if (selectedIndex == correct) {
            // Correct — turn green
            score += 10;
            optionButtons[selectedIndex].setBackgroundColor(Color.parseColor("#4CAF50"));
            optionButtons[selectedIndex].setTextColor(Color.WHITE);
        } else {
            // Wrong — turn red, also show correct in green
            optionButtons[selectedIndex].setBackgroundColor(Color.parseColor("#F44336"));
            optionButtons[selectedIndex].setTextColor(Color.WHITE);
            optionButtons[correct].setBackgroundColor(Color.parseColor("#4CAF50"));
            optionButtons[correct].setTextColor(Color.WHITE);
        }

        tvScore.setText("⭐ " + score);

        // Wait 1.5 seconds then go to next question
        new Handler().postDelayed(() -> {
            currentIndex++;
            loadQuestion();
        }, 1500);
    }

    private void showResults() {
        // Save earned points to SharedPreferences (so Progress screen can read it)
        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        int existing = prefs.getInt("totalPoints", 0);
        prefs.edit().putInt("totalPoints", existing + score).apply();

        // Pick a message based on score
        String emoji, message;
        int maxScore = questions.size() * 10;
        if (score >= maxScore * 0.8) {
            emoji = "🌟"; message = "Excellent! You're a superstar!";
        } else if (score >= maxScore * 0.5) {
            emoji = "😊"; message = "Good job! Keep practicing!";
        } else {
            emoji = "💪"; message = "Keep trying! You'll get better!";
        }

        new AlertDialog.Builder(this)
                .setTitle(emoji + " Quiz Complete!")
                .setMessage(message + "\n\nYou scored " + score + " out of " + maxScore + " points!")
                .setPositiveButton("🏠 Go Home", (d, w) -> finish())
                .setNegativeButton("🔄 Try Again", (d, w) -> {
                    currentIndex = 0;
                    score = 0;
                    loadQuestion();
                })
                .setCancelable(false)
                .show();
    }

    // ─── QUESTIONS PER SUBJECT ───────────────────────────────────────────────

    private List<Question> getQuestionsForSubject(String subject) {
        List<Question> list = new ArrayList<>();

        switch (subject) {
            case "Math":
                list.add(new Question("What is 5 + 7?",
                        new String[]{"10", "12", "11", "13"}, 1));
                list.add(new Question("What is 9 × 3?",
                        new String[]{"27", "24", "21", "30"}, 0));
                list.add(new Question("What is 20 ÷ 4?",
                        new String[]{"6", "4", "5", "8"}, 2));
                list.add(new Question("What is 15 - 8?",
                        new String[]{"8", "6", "9", "7"}, 3));
                list.add(new Question("What is 6²?",
                        new String[]{"12", "36", "18", "30"}, 1));
                break;

            case "English":
                list.add(new Question("Which word is a noun?",
                        new String[]{"Run", "Happy", "Apple", "Quickly"}, 2));
                list.add(new Question("Plural of 'child' is?",
                        new String[]{"Childs", "Childes", "Children", "Childrens"}, 2));
                list.add(new Question("Which sentence is correct?",
                        new String[]{"She go to school", "She goes to school",
                                "She going to school", "She gone"}, 1));
                list.add(new Question("'Enormous' means?",
                        new String[]{"Very small", "Very fast", "Very loud", "Very large"}, 3));
                list.add(new Question("What punctuation ends a question?",
                        new String[]{".", "!", "?", ","}, 2));
                break;

            case "Science":
                list.add(new Question("What do plants need to make food?",
                        new String[]{"Water only", "Sunlight only",
                                "Sunlight, water & CO₂", "Soil only"}, 2));
                list.add(new Question("Closest planet to the Sun?",
                        new String[]{"Venus", "Earth", "Mars", "Mercury"}, 3));
                list.add(new Question("What state of matter is ice?",
                        new String[]{"Gas", "Liquid", "Plasma", "Solid"}, 3));
                list.add(new Question("Which organ pumps blood?",
                        new String[]{"Lungs", "Brain", "Heart", "Liver"}, 2));
                list.add(new Question("H₂O is?",
                        new String[]{"Salt", "Water", "Air", "Sugar"}, 1));
                break;

            default:
                list.add(new Question("How many days in a week?",
                        new String[]{"5", "6", "7", "8"}, 2));
                list.add(new Question("Blue + Yellow makes?",
                        new String[]{"Purple", "Green", "Orange", "Brown"}, 1));
                list.add(new Question("How many months in a year?",
                        new String[]{"10", "11", "12", "13"}, 2));
                list.add(new Question("Largest ocean on Earth?",
                        new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));
                list.add(new Question("Sides of a triangle?",
                        new String[]{"2", "3", "4", "5"}, 1));
                break;
        }

        return list;
    }
}