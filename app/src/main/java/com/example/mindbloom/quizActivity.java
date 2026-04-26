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

    private TextView tvQuestion, tvScore, tvQuestionCount, tvSubjectLabel;
    private ProgressBar progressBar;
    private Button[] optionButtons;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private String subject = "General";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        subject = getIntent().getStringExtra("subject");
        if (subject == null) subject = "General";

        tvQuestion      = findViewById(R.id.tvQuestion);
        tvScore         = findViewById(R.id.tvScore);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvSubjectLabel  = findViewById(R.id.tvSubjectLabel);
        progressBar     = findViewById(R.id.progressBar);

        if (tvSubjectLabel != null) tvSubjectLabel.setText(subject + " Quiz");

        optionButtons = new Button[]{
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        };

        questions = getQuestionsForSubject(subject);
        progressBar.setMax(questions.size());
        loadQuestion();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> checkAnswer(index));
        }
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) { showResults(); return; }
        answered = false;
        Question q = questions.get(currentIndex);
        tvQuestion.setText(q.getQuestionText());
        tvQuestionCount.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        tvScore.setText("⭐ " + score);
        progressBar.setProgress(currentIndex + 1);
        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(opts[i]);
            optionButtons[i].setBackgroundColor(Color.WHITE);
            optionButtons[i].setTextColor(Color.parseColor("#333333"));
            optionButtons[i].setEnabled(true);
        }
    }

    private void checkAnswer(int selectedIndex) {
        if (answered) return;
        answered = true;
        for (Button btn : optionButtons) btn.setEnabled(false);
        int correct = questions.get(currentIndex).getCorrectAnswerIndex();
        if (selectedIndex == correct) {
            score += 10;
            optionButtons[selectedIndex].setBackgroundColor(Color.parseColor("#4CAF50"));
            optionButtons[selectedIndex].setTextColor(Color.WHITE);
        } else {
            optionButtons[selectedIndex].setBackgroundColor(Color.parseColor("#F44336"));
            optionButtons[selectedIndex].setTextColor(Color.WHITE);
            optionButtons[correct].setBackgroundColor(Color.parseColor("#4CAF50"));
            optionButtons[correct].setTextColor(Color.WHITE);
        }
        tvScore.setText("⭐ " + score);
        new Handler().postDelayed(() -> { currentIndex++; loadQuestion(); }, 1500);
    }

    private void showResults() {
        SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
        int existing       = prefs.getInt("totalPoints", 0);
        int quizzesDone    = prefs.getInt("quizzesDone", 0);
        int bestForSubject = prefs.getInt("best_" + subject, 0);
        prefs.edit()
             .putInt("totalPoints", existing + score)
             .putInt("quizzesDone", quizzesDone + 1)
             .putInt("best_" + subject, Math.max(bestForSubject, score))
             .apply();

        long today   = System.currentTimeMillis() / 86400000L;
        long lastDay = prefs.getLong("lastQuizDay", -1);
        int streak   = prefs.getInt("streak", 0);
        if (lastDay == today - 1) streak++;
        else if (lastDay != today) streak = 1;
        prefs.edit().putInt("streak", streak).putLong("lastQuizDay", today).apply();

        String emoji, message;
        int maxScore = questions.size() * 10;
        if (score >= maxScore * 0.8)      { emoji = "🌟"; message = "Excellent! You're a superstar!"; }
        else if (score >= maxScore * 0.5) { emoji = "😊"; message = "Good job! Keep practicing!"; }
        else                              { emoji = "💪"; message = "Keep trying! You'll get better!"; }

        new AlertDialog.Builder(this)
            .setTitle(emoji + " Quiz Complete!")
            .setMessage(message + "\n\nYou scored " + score + " / " + maxScore + " points!\n🔥 " + streak + " day streak!")
            .setPositiveButton("🏠 Go Home", (d, w) -> finish())
            .setNegativeButton("🔄 Try Again", (d, w) -> { currentIndex = 0; score = 0; loadQuestion(); })
            .setCancelable(false).show();
    }

    private List<Question> getQuestionsForSubject(String subject) {
        List<Question> list = new ArrayList<>();
        switch (subject) {
            case "Math":
                list.add(new Question("What is 5 + 7?",         new String[]{"10","12","11","13"}, 1));
                list.add(new Question("What is 9 × 3?",         new String[]{"27","24","21","30"}, 0));
                list.add(new Question("What is 20 ÷ 4?",        new String[]{"6","4","5","8"}, 2));
                list.add(new Question("What is 15 - 8?",        new String[]{"8","6","9","7"}, 3));
                list.add(new Question("What is 6²?",            new String[]{"12","36","18","30"}, 1));
                break;
            case "English":
                list.add(new Question("Which word is a noun?",  new String[]{"Run","Happy","Apple","Quickly"}, 2));
                list.add(new Question("Plural of 'child'?",     new String[]{"Childs","Childes","Children","Childrens"}, 2));
                list.add(new Question("Which sentence is correct?", new String[]{"She go","She goes","She going","She gone"}, 1));
                list.add(new Question("'Enormous' means?",      new String[]{"Very small","Very fast","Very loud","Very large"}, 3));
                list.add(new Question("What ends a question?",  new String[]{".","!","?",","}, 2));
                break;
            case "Science":
                list.add(new Question("Plants make food using?", new String[]{"Water only","Sunlight only","Sunlight, water & CO₂","Soil only"}, 2));
                list.add(new Question("Closest planet to Sun?", new String[]{"Venus","Earth","Mars","Mercury"}, 3));
                list.add(new Question("State of matter of ice?",new String[]{"Gas","Liquid","Plasma","Solid"}, 3));
                list.add(new Question("Which organ pumps blood?",new String[]{"Lungs","Brain","Heart","Liver"}, 2));
                list.add(new Question("H₂O is?",               new String[]{"Salt","Water","Air","Sugar"}, 1));
                break;
            case "Urdu":
                list.add(new Question("What does پانی mean?",  new String[]{"Book","School","Water","Friend"}, 2));
                list.add(new Question("Urdu is written?",      new String[]{"Left to right","Right to left","Top to bottom","Bottom to top"}, 1));
                list.add(new Question("How many letters in Urdu?", new String[]{"26","32","38","40"}, 2));
                list.add(new Question("What does کتاب mean?",  new String[]{"Water","Book","School","Mother"}, 1));
                list.add(new Question("Pakistan's national language?", new String[]{"English","Arabic","Persian","Urdu"}, 3));
                break;
            case "History":
                list.add(new Question("Pakistan was created in?", new String[]{"1945","1947","1950","1952"}, 1));
                list.add(new Question("Founder of Pakistan?", new String[]{"Allama Iqbal","Liaquat Ali Khan","Quaid-e-Azam Jinnah","Ayub Khan"}, 2));
                list.add(new Question("First writing invented in?", new String[]{"Egypt","China","Mesopotamia","India"}, 2));
                list.add(new Question("Pyramids were built in?", new String[]{"Greece","Rome","Egypt","Persia"}, 2));
                list.add(new Question("Pakistan Resolution year?", new String[]{"1930","1935","1940","1945"}, 2));
                break;
            case "Art":
                list.add(new Question("Red + Blue = ?",        new String[]{"Orange","Green","Purple","Brown"}, 2));
                list.add(new Question("Blue + Yellow = ?",     new String[]{"Purple","Green","Orange","Pink"}, 1));
                list.add(new Question("Who painted Mona Lisa?",new String[]{"Van Gogh","Picasso","Da Vinci","Kahlo"}, 2));
                list.add(new Question("How many primary colours?", new String[]{"3","4","5","6"}, 0));
                list.add(new Question("Red + Yellow = ?",      new String[]{"Purple","Green","Orange","Pink"}, 2));
                break;
            default:
                list.add(new Question("Days in a week?",       new String[]{"5","6","7","8"}, 2));
                list.add(new Question("Blue + Yellow = ?",     new String[]{"Purple","Green","Orange","Brown"}, 1));
                list.add(new Question("Months in a year?",     new String[]{"10","11","12","13"}, 2));
                list.add(new Question("Largest ocean?",        new String[]{"Atlantic","Indian","Arctic","Pacific"}, 3));
                list.add(new Question("Sides of a triangle?",  new String[]{"2","3","4","5"}, 1));
                break;
        }
        return list;
    }
}
