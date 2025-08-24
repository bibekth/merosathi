package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.merosathi.adapter.NotificationAdapter;
import com.example.merosathi.model.Notification;
import com.example.merosathi.model.NotificationResponse;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageView ivProfileIcon;
    LinearLayout llBabyGrowth, llBodyChange, llArticle, llHome;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, profileIntent, homeIntent;
    String token, bearerToken;
    RecyclerView rvNotification;

    NotificationAdapter adapter;
    ArrayList<Notification> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initSharedPreference();
        initIntents();
        initViews();
        initClickEvents();
        setupRecyclerView();
        fetchNotifications();
    }

    private void initSharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void initIntents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        homeIntent = new Intent(this, MainActivity.class);
    }

    private void initViews() {
        ivProfileIcon = findViewById(R.id.ivProfileIcon);
        llHome = findViewById(R.id.llHome);
        llArticle = findViewById(R.id.llArticle);
        llBodyChange = findViewById(R.id.llBodyChange);
        llBabyGrowth = findViewById(R.id.llBabyGrowth);
        rvNotification = findViewById(R.id.rvNotification);
    }

    private void initClickEvents() {
        llBabyGrowth.setOnClickListener(v -> startActivity(babyGrowthIntent));
        llBodyChange.setOnClickListener(v -> startActivity(bodyChangeIntent));
        llArticle.setOnClickListener(v -> startActivity(articleIntent));
        llHome.setOnClickListener(v -> startActivity(homeIntent));
        ivProfileIcon.setOnClickListener(v -> startActivity(profileIntent));
    }

    private void setupRecyclerView() {
        rvNotification.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(this, notificationList, notification -> {
            // Pass notification id to SingleNotificationActivity
            Intent intent = new Intent(NotificationActivity.this, SingleNotificationActivity.class);
            intent.putExtra("id", notification.getId()); // Ensure Notification model has getId()
            intent.putExtra("token", token);
            startActivity(intent);
        });

        rvNotification.setAdapter(adapter);
    }

    private void fetchNotifications() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);

        Call<NotificationResponse> call = apiService.notifications(bearerToken);

        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notifications = response.body().getData();
                    if (notifications != null && !notifications.isEmpty()) {
                        adapter.setData(notifications);
                    } else {
                        Log.d("API", "No notifications available");
                    }
                } else {
                    Log.e("API_ERROR", "Response failed: " + response.message());
                    Toast.makeText(NotificationActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage(), t);
                Toast.makeText(NotificationActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
