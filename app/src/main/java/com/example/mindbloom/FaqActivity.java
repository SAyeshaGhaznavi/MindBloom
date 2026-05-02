package com.example.mindbloom;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class FaqActivity extends AppCompatActivity {

    private final String[][] faqs = {
        {"What is MindBloom?", "MindBloom is a fun learning app that helps students study through lessons, quizzes, and homework tracking across multiple subjects."},
        {"How do I take a quiz?", "Tap 'Quizzes' on the dashboard, pick your subject, complete the lesson, then tap 'Take Quiz'. You earn points for every correct answer!"},
        {"How are points earned?", "You earn 10 points per correct quiz answer and 5 points for completing homework tasks. Points add up to unlock badges and level up!"},
        {"What are the badge levels?", "There are 6 badges: 🌱 Sprout (0 pts), 🌿 Seedling (50 pts), 🌸 Blossom (150 pts), ⭐ Star (300 pts), 🏆 Champion (500 pts), 💎 Legend (1000 pts)."},
        {"How do I change my name?", "Tap the 👤 profile icon on the dashboard to open Profile Settings and update your name."},
        {"Can I switch between dark and light mode?", "Yes! Go to Settings (from the dashboard) and toggle Dark Mode on or off anytime."},
        {"How do I change font size?", "Open Settings and use the font size slider to choose Small, Medium, or Large text."},
        {"What subjects are available?", "MindBloom currently offers Math, English, Science, Urdu, History, and Art."},
        {"How does homework work?", "Tap 'Homework' on the dashboard to see your tasks. Check off completed tasks to earn points!"},
        {"How do I track my progress?", "Tap 'View My Progress' on the dashboard to see your total points, badges, and per-subject stats."}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ImageButton tvBack = findViewById(R.id.tvBack);
        TextView tvBreadcrumb = findViewById(R.id.tvBreadcrumb);
        LinearLayout faqContainer = findViewById(R.id.faqContainer);

        tvBreadcrumb.setText("Home > FAQ & Help");
        tvBack.setOnClickListener(v -> finish());

        for (String[] faq : faqs) {
            View item = getLayoutInflater().inflate(R.layout.item_faq, faqContainer, false);
            TextView tvQ = item.findViewById(R.id.tvQuestion);
            TextView tvA = item.findViewById(R.id.tvAnswer);
            tvQ.setText("❓ " + faq[0]);
            tvA.setText(faq[1]);
            tvA.setVisibility(View.GONE);

            // Toggle answer on tap (HCI: progressive disclosure)
            tvQ.setOnClickListener(v -> {
                if (tvA.getVisibility() == View.VISIBLE) {
                    tvA.setVisibility(View.GONE);
                    tvQ.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0);
                } else {
                    tvA.setVisibility(View.VISIBLE);
                    tvQ.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0);
                }
            });

            faqContainer.addView(item);
        }
    }
}
