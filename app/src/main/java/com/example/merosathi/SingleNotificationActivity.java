package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.merosathi.model.SingleNotificationResponse;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleNotificationActivity extends AppCompatActivity {
    ImageView ivProfileIcon;
    private TextView tvNotificationTitle, tvNotificationDescription;
    private ApiService apiService;
    private String token;
    int notificationId;
    LinearLayout llBabyGrowth, llBodyChange, llArticle, llHome;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, profileIntent, homeIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_notification);

        initIntents();
        initViews();
        initClickEvents();

        tvNotificationTitle = findViewById(R.id.tvNotificationTitle);
        tvNotificationDescription = findViewById(R.id.tvNotificationDescription);

        apiService = RetrofitService.getService(this).create(ApiService.class);

        // Get token and notificationId from intent
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        notificationId = intent.getIntExtra("id", -1);
        if (notificationId != -1 && token != null) {
            fetchNotification(notificationId);
        } else {
            Toast.makeText(this, "Invalid notification data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchNotification(int id) {
        Call<SingleNotificationResponse> call = apiService.singleNotification("Bearer " + token, id);

        call.enqueue(new Callback<SingleNotificationResponse>() {
            @Override
            public void onResponse(Call<SingleNotificationResponse> call, Response<SingleNotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    tvNotificationTitle.setText(response.body().getData().getTitle());
                    tvNotificationDescription.setText(response.body().getData().getDescription());
                } else {
                    Toast.makeText(SingleNotificationActivity.this, "Failed to load notification", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<SingleNotificationResponse> call, Throwable t) {
                Toast.makeText(SingleNotificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "onFailure: ", t);
            }
        });
    }

    private void initViews() {
        ivProfileIcon = findViewById(R.id.ivProfileIcon);
        llHome = findViewById(R.id.llHome);
        llArticle = findViewById(R.id.llArticle);
        llBodyChange = findViewById(R.id.llBodyChange);
        llBabyGrowth = findViewById(R.id.llBabyGrowth);
    }

    private void initClickEvents() {
        llBabyGrowth.setOnClickListener(v -> startActivity(babyGrowthIntent));
        llBodyChange.setOnClickListener(v -> startActivity(bodyChangeIntent));
        llArticle.setOnClickListener(v -> startActivity(articleIntent));
        llHome.setOnClickListener(v -> startActivity(homeIntent));
        ivProfileIcon.setOnClickListener(v -> startActivity(profileIntent));
    }

    private void initIntents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        homeIntent = new Intent(this, MainActivity.class);
    }
}
