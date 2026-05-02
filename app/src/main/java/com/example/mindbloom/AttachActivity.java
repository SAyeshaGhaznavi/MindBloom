package com.example.mindbloom;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AttachActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Setup Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");
                    tab.setIcon(R.drawable.home);
                    break;
                case 1:
                    tab.setText("Progress");
                    tab.setIcon(R.drawable.progress);
                    break;
                case 2:
                    tab.setText("Settings");
                    tab.setIcon(R.drawable.settings);
                    break;
                case 3:
                    tab.setText("Profile");
                    tab.setIcon(R.drawable.account);
                    break;
            }
        }).attach();
    }
    public void switchTab(int position) {
        viewPager.setCurrentItem(position, true);
    }
}