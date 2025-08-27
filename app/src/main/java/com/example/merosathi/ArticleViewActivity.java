package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.merosathi.adapter.ReferenceAdapter;
import com.example.merosathi.adapter.SectionAdapter;
import com.example.merosathi.model.Article;
import com.example.merosathi.model.BabyGrowth;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleViewActivity extends AppCompatActivity {
    Integer id;
    String token, bearerToken, docContent;
    TextView tvTitle, tvDoctorDescription, tvContent;
    RecyclerView rvSection, rvReferences;
    ImageView ivBannerImage, ivDoctorAvatar;
    SectionAdapter sectionAdapter;
    ReferenceAdapter referenceAdapter;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayList<Article.Data.Section> sectionArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        id = getIntent().getIntExtra("id", 1);
        sharedPreference();
        viewFinder();
        fetchData();
        topSection();
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
        rvSection = findViewById(R.id.rvSection);
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void fetchData() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<Article> call = apiService.articleShow(bearerToken, id);

        call.enqueue(new Callback<Article>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if(response.isSuccessful()) {
                    Article article = response.body();
                    assert article != null;
                    tvTitle.setText(article.getData().getTitle());
                    tvContent.setText(article.getData().getDescription());
                    Glide.with(ArticleViewActivity.this)
                            .load(SharedPreferenceManager.getUrl() + article.getData().getBanner_image())
                            .into(ivBannerImage);
                    docContent = article.getData().getUser().getDoctor().getName() + " | " + article.getData().getUser().getDoctor().getDescription();
                    tvDoctorDescription.setText(docContent);

                    stringArrayList.clear();
                    if(article.getData().getReferences() != null) {
                        stringArrayList.addAll(article.getData().getReferences());
                    }
                    setReferenceAdapter();
                    referenceAdapter.notifyDataSetChanged();

                    sectionArrayList.clear();
                    if(article.getData().getSections() != null) {
                        sectionArrayList.addAll(article.getData().getSections());
                    }
                    setSectionAdapter();
                    sectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    private void setSectionAdapter() {
        sectionAdapter = new SectionAdapter(this, sectionArrayList);
        rvSection.setLayoutManager(new LinearLayoutManager(this));
        rvSection.setAdapter(sectionAdapter);
    }

    private void setReferenceAdapter() {
        referenceAdapter = new ReferenceAdapter(this, stringArrayList);
        rvReferences.setLayoutManager(new LinearLayoutManager(this));
        rvReferences.setAdapter(referenceAdapter);
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
}