package com.example.mindbloom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.*;

public class LessonActivity extends AppCompatActivity {

    private TextView tvSubjectTitle, tvLessonTitle, tvLessonBody, tvPageCount;
    private Button btnPrev, btnNext, btnTakeQuiz;
    private ProgressBar lessonProgress;

    private ImageButton btnAudioPlay, btnTimerToggle;
    private SeekBar audioSeekBar, seekBarAudio, seekBarSpeed;
    private Switch switchSignLanguage, switchSubtitles, switchInterpreter;
    private TextView tvAudioTime, tvSpeedLabel, tvFocusTimer;

    private List<String[]> pages; // each String[]: {title, body}
    private int currentPage = 0;
    private String subject = "Math";

    private boolean isAudioPlaying = false;

    private CountDownTimer focusTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 600000; // 10 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        subject = getIntent().getStringExtra("subject");
        if (subject == null) subject = "Math";

        tvSubjectTitle = findViewById(R.id.tvLessonSubject);
        tvLessonTitle  = findViewById(R.id.tvLessonTitle);
        tvLessonBody   = findViewById(R.id.tvLessonBody);
        tvPageCount    = findViewById(R.id.tvLessonPageCount);
        lessonProgress = findViewById(R.id.lessonProgressBar);
        btnPrev        = findViewById(R.id.btnLessonPrev);
        btnNext        = findViewById(R.id.btnLessonNext);
        btnTakeQuiz    = findViewById(R.id.btnTakeQuiz);

        btnTimerToggle     = findViewById(R.id.btnTimerToggle);
        seekBarAudio       = findViewById(R.id.seekBarAudio);
        seekBarSpeed       = findViewById(R.id.seekBarSpeed);
        switchSignLanguage = findViewById(R.id.switchSignLanguage);
        switchSubtitles    = findViewById(R.id.switchSubtitles);
        switchInterpreter  = findViewById(R.id.switchInterpreter);
        tvSpeedLabel       = findViewById(R.id.tvSpeedLabel);
        tvFocusTimer       = findViewById(R.id.tvFocusTimer);

        tvSubjectTitle.setText(subject + " Lesson");

        pages = getLessonPages(subject);
        lessonProgress.setMax(pages.size());
        showPage(0);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) showPage(currentPage - 1);
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < pages.size() - 1) showPage(currentPage + 1);
        });

        btnTakeQuiz.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MindBloomPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("lesson_done_" + subject, true).apply();

            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("subject", subject);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnLessonBack).setOnClickListener(v -> finish());


        // Speed slider
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = progress / 100f;
                tvSpeedLabel.setText(String.format(Locale.US, "%.1fx", speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Sign Language Stream toggle
        switchSignLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Sign Language Stream ON" : "Audio-Only Mode", Toast.LENGTH_SHORT).show();
        });

        // Subtitles toggle
        switchSubtitles.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Subtitles ON" : "Subtitles OFF", Toast.LENGTH_SHORT).show();
        });

        // Sign Language Interpreter toggle
        switchInterpreter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Sign Language Interpreter ON" : "Sign Language Interpreter OFF", Toast.LENGTH_SHORT).show();
        });

        // Focus Timer
        btnTimerToggle.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        updateTimerDisplay();
    }

    private void startAudioSimulation() {
        Thread thread = new Thread(() -> {
            int current = audioSeekBar.getProgress();
            while (isAudioPlaying && current < audioSeekBar.getMax()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                current++;
                int finalCurrent = current;
                runOnUiThread(() -> {
                    audioSeekBar.setProgress(finalCurrent);
                    int mins = finalCurrent / 60;
                    int secs = finalCurrent % 60;
                    tvAudioTime.setText(String.format(Locale.US, "%d:%02d", mins, secs));
                });
            }
            if (current >= audioSeekBar.getMax()) {
                runOnUiThread(() -> {
                    isAudioPlaying = false;
                    btnAudioPlay.setImageResource(android.R.drawable.ic_media_play);
                    audioSeekBar.setProgress(0);
                    tvAudioTime.setText("0:00");
                });
            }
        });
        thread.start();
    }

    private void startTimer() {
        focusTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                btnTimerToggle.setImageResource(android.R.drawable.ic_media_play);
                timeLeftInMillis = 600000;
                updateTimerDisplay();
                Toast.makeText(LessonActivity.this, "Focus time is up! Great job! 🌟", Toast.LENGTH_LONG).show();
            }
        }.start();

        isTimerRunning = true;
        btnTimerToggle.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void pauseTimer() {
        if (focusTimer != null) {
            focusTimer.cancel();
        }
        isTimerRunning = false;
        btnTimerToggle.setImageResource(android.R.drawable.ic_media_play);
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvFocusTimer.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
    }

    private void showPage(int index) {
        currentPage = index;
        String[] page = pages.get(index);
        tvLessonTitle.setText(page[0]);
        tvLessonBody.setText(page[1]);
        tvPageCount.setText((index + 1) + " / " + pages.size());
        lessonProgress.setProgress(index + 1);

        btnPrev.setEnabled(index > 0);
        btnPrev.setAlpha(index > 0 ? 1f : 0.4f);
        btnNext.setEnabled(index < pages.size() - 1);
        btnNext.setAlpha(index < pages.size() - 1 ? 1f : 0.4f);

        btnTakeQuiz.setVisibility(index == pages.size() - 1 ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private List<String[]> getLessonPages(String subject) {
        List<String[]> list = new ArrayList<>();
        switch (subject) {
            case "Math":
                list.add(new String[]{"What is Multiplication? ✖️",
                        "Multiplication is repeated addition!\n\nInstead of writing 4 + 4 + 4, we write 4 × 3 = 12.\n\nThe × symbol means 'groups of'.\n\nSo 4 × 3 means '4 groups of 3 things'."});
                list.add(new String[]{"Multiplication Table Tips 📊",
                        "Here's a trick for the 9 times table:\n\n9 × 1 = 9\n9 × 2 = 18\n9 × 3 = 27\n\nNotice: the digits of the answer always add up to 9!\n1+8=9, 2+7=9 ✨\n\nTry it for 9 × 4 = 36 → 3+6 = 9!"});
                list.add(new String[]{"Practice Makes Perfect! 🌟",
                        "Let's practise what we learned:\n\n• 5 × 6 = ?\n• 7 × 8 = ?\n• 9 × 9 = ?\n\nAnswers: 30, 56, 81\n\nNow take the quiz to earn your stars! ⭐"});
                break;
            case "English":
                list.add(new String[]{"Parts of Speech 📝",
                        "Every word in English has a job!\n\n• NOUN — a person, place or thing\n  (dog, school, happiness)\n\n• VERB — an action or state\n  (run, think, is)\n\n• ADJECTIVE — describes a noun\n  (big, happy, red)"});
                list.add(new String[]{"Nouns vs Verbs 🔍",
                        "Can you spot the difference?\n\n'The CAT (noun) RUNS (verb) fast.'\n\nA quick trick:\n→ If you can add 'the' before it → it's probably a noun\n→ If you can say 'I ___' → it's probably a verb\n\nTry: 'jump', 'book', 'smile', 'cloud'"});
                list.add(new String[]{"Adjectives Make Stories Alive! ✨",
                        "Compare these sentences:\n\n❌ 'The dog sat on the mat.'\n✅ 'The fluffy, tiny dog sat on the warm, cosy mat.'\n\nAdjectives make writing vivid and interesting!\n\nChallenge: add 2 adjectives to this:\n'The girl ate an apple.'"});
                break;
            case "Science":
                list.add(new String[]{"What is Photosynthesis? 🌿",
                        "Plants make their own food using:\n\n☀️ Sunlight\n💧 Water (from roots)\n🌬️ Carbon dioxide (from air)\n\nInside leaves, a green chemical called CHLOROPHYLL captures sunlight and uses it to turn water + CO₂ into sugar and oxygen.\n\nWe breathe the oxygen plants release!"});
                list.add(new String[]{"The Water Cycle 💧",
                        "Water travels in a never-ending cycle:\n\n1. EVAPORATION — sun heats water, it rises as vapour\n2. CONDENSATION — vapour cools and forms clouds\n3. PRECIPITATION — rain/snow falls back down\n4. COLLECTION — gathers in rivers, lakes, oceans\n\n...and the cycle begins again! 🔄"});
                list.add(new String[]{"Our Solar System 🪐",
                        "There are 8 planets in our solar system:\n\n☿ Mercury — closest to Sun, very hot!\n♀ Venus — brightest planet\n🌍 Earth — our home!\n♂ Mars — the red planet\n♃ Jupiter — largest planet\n♄ Saturn — has beautiful rings\n♅ Uranus — rotates on its side\n♆ Neptune — furthest from Sun"});
                break;
            case "Urdu":
                list.add(new String[]{"اردو حروف تہجی 🌸",
                        "Urdu has 38 letters!\n\nUrdu is written from RIGHT to LEFT.\n\nThe first letter is: ا (Alif)\n\nSome common letters:\nب = B sound\nپ = P sound\nت = T sound\nج = J sound\n\nUrdu is the national language of Pakistan 🇵🇰"});
                list.add(new String[]{"Simple Urdu Words 📖",
                        "Let's learn some words:\n\nپانی (Paani) = Water\nکتاب (Kitaab) = Book\nاسکول (Iskool) = School\nدوست (Dost) = Friend\nماں (Maan) = Mother\nباپ (Baap) = Father\n\nPractice saying these out loud!"});
                list.add(new String[]{"Urdu Sentence Structure 💬",
                        "In Urdu, the verb comes at the END of the sentence!\n\nEnglish: 'I eat food.'\nUrdu: 'میں کھانا کھاتا ہوں'\n(Main khaana khaata hoon)\n\nLiterally: 'I food eat am'\n\nThis is opposite to English word order — cool isn't it?"});
                break;
            case "History":
                list.add(new String[]{"Ancient Civilizations 🏛️",
                        "Some of the world's oldest civilizations:\n\n🌍 Mesopotamia (modern Iraq) — first writing invented here!\n🐊 Ancient Egypt — famous for pyramids & pharaohs\n🕌 Indus Valley — planned cities with drainage systems!\n🐉 Ancient China — invented paper and silk\n\nAll began near rivers — why? Because water = food = life!"});
                list.add(new String[]{"Pakistan's History 🇵🇰",
                        "Pakistan was founded on 14th August 1947.\n\nQuaid-e-Azam Muhammad Ali Jinnah was the founder.\n\nBefore partition, the subcontinent was ruled by:\n• Mughal Empire (1526–1857)\n• British Empire (1857–1947)\n\nThe name 'Pakistan' means 'Land of the Pure' in Urdu and Persian."});
                list.add(new String[]{"Timeline of History 📅",
                        "Key dates to remember:\n\n3000 BC — Egyptian pyramids built\n1526 AD — Mughal Empire founded\n1857 AD — British took full control of India\n1906 AD — Muslim League founded\n1940 AD — Pakistan Resolution passed\n1947 AD — Pakistan created!\n\nHistory helps us understand how the present came to be."});
                break;
            case "Art":
                list.add(new String[]{"Primary Colours 🎨",
                        "There are 3 PRIMARY colours — colours you cannot make by mixing others:\n\n🔴 Red\n🔵 Blue\n🟡 Yellow\n\nWhen you MIX two primary colours you get SECONDARY colours:\n\n🔴 Red + 🔵 Blue = 🟣 Purple\n🔵 Blue + 🟡 Yellow = 🟢 Green\n🔴 Red + 🟡 Yellow = 🟠 Orange"});
                list.add(new String[]{"Elements of Art ✏️",
                        "Every artwork is built from these elements:\n\n• LINE — straight, curved, zigzag\n• SHAPE — circle, square, triangle\n• COLOUR — hue, shade, tint\n• TEXTURE — rough, smooth, bumpy\n• SPACE — the area around shapes\n• VALUE — light vs dark\n\nArtists combine these to create beautiful works!"});
                list.add(new String[]{"Famous Artists 🖼️",
                        "Some artists who changed the world:\n\n🇮🇹 Leonardo da Vinci — painted the Mona Lisa\n🇳🇱 Vincent van Gogh — famous for Starry Night\n🇪🇸 Pablo Picasso — invented Cubism\n🇲🇽 Frida Kahlo — known for self-portraits\n\nYou can be an artist too! Art is about expressing how YOU see the world 🌸"});
                break;
            default:
                list.add(new String[]{"Welcome to MindBloom! 🌸",
                        "MindBloom is your fun learning companion!\n\nYou can:\n• Study different subjects\n• Take quizzes to earn stars ⭐\n• Track your progress\n• Earn cool badges 🏅\n\nLet's start learning!"});
                break;
        }
        return list;
    }
}