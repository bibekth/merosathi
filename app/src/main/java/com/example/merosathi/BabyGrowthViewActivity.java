package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.merosathi.adapter.ReferenceAdapter;
import com.example.merosathi.model.BabyGrowth;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BabyGrowthViewActivity extends AppCompatActivity {
    TextView tvTitle, tvDoctorDescription, tvContent;
    ImageView ivDoctorAvatar, ivBannerImage;
    String token, bearerToken, docContent;
    RecyclerView rvReferences;
    Integer id;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ReferenceAdapter referenceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_growth_view);


        id = getIntent().getIntExtra("id", 1);
        viewFinder();
        sharedPreference();
        fetchData();
        topSection();
    }
    private void topSection() {
        ImageView ivProfileIcon = findViewById(R.id.ivProfileIcon);
        ImageView ivNotification = findViewById(R.id.ivNotification);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        ivProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(profileIntent);
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(notificationIntent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void viewFinder() {
        tvTitle = findViewById(R.id.tvTitle);
        tvDoctorDescription = findViewById(R.id.tvDoctorDescription);
        tvContent = findViewById(R.id.tvContent);
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        ivBannerImage = findViewById(R.id.ivBannerImage);
        rvReferences = findViewById(R.id.rvReferences);
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void fetchData() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<BabyGrowth> call = apiService.babyGrowthShow(bearerToken, id);

        call.enqueue(new Callback<BabyGrowth>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BabyGrowth> call, Response<BabyGrowth> response) {
                if(response.isSuccessful()) {
                    BabyGrowth babyGrowth = response.body();
                    assert babyGrowth != null;
                    tvTitle.setText(babyGrowth.getData().getTitle());
                    tvContent.setText(babyGrowth.getData().getDescription());
                    Glide.with(BabyGrowthViewActivity.this)
                            .load(SharedPreferenceManager.getUrl() + babyGrowth.getData().getBanner_image())
                            .into(ivBannerImage);
                    docContent = babyGrowth.getData().getUser().getDoctor().getName() + " | " + babyGrowth.getData().getUser().getDoctor().getDescription();
                    tvDoctorDescription.setText(docContent);

                    stringArrayList.clear();
                    if(babyGrowth.getData().getReferences() != null) {
                        stringArrayList.addAll(babyGrowth.getData().getReferences());
                    }
                    setReferenceAdapter();
                    referenceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BabyGrowth> call, Throwable t) {

            }
        });
    }

    private void setReferenceAdapter() {
        referenceAdapter = new ReferenceAdapter(this, stringArrayList);
        rvReferences.setLayoutManager(new LinearLayoutManager(this));
        rvReferences.setAdapter(referenceAdapter);
    }

}