package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvHelloIam, tvMeroSathi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivLogo = findViewById(R.id.ivLogo);
        tvHelloIam = findViewById(R.id.tvHelloIam);
        tvMeroSathi = findViewById(R.id.tvMeroSathi);

        // Load animation
        Animation zoomInLogo = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        ivLogo.startAnimation(zoomInLogo);

        new Handler().postDelayed(() -> tvHelloIam.setVisibility(View.VISIBLE), 500);

        new Handler().postDelayed(() -> tvMeroSathi.setVisibility(View.VISIBLE), 1000);

        Intent splashIntent = new Intent(this, LoginActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(splashIntent);
            }
        }, 1500);
    }
}