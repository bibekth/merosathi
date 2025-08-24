package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.merosathi.adapter.OnboardingAdapter;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private int[] layouts = {
            R.layout.onboarding_screen_1,
            R.layout.onboarding_screen_2,
            R.layout.onboarding_screen_3,
            R.layout.onboarding_screen_4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);

        viewPager.setAdapter(new OnboardingAdapter(layouts));

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < layouts.length - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
