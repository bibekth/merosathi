package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.merosathi.adapter.ArticleAdapter;
import com.example.merosathi.model.ArticleList;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity {
    ArticleAdapter articleAdapter;
    ArrayList<ArticleList.Data> dataArrayList = new ArrayList<>();
    Integer growthId;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, homeIntent;
    RecyclerView recyclerView;
    String token, bearerToken;
    LinearLayout llBabyGrowth, llBodyChange, llArticle, llHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        sharedPreference();
        viewFinder();
        intents();
        fetchBodyChange();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClickEvents();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void viewFinder()
    {
        recyclerView = findViewById(R.id.rvBabyGrowth);
        llBabyGrowth = findViewById(R.id.llBabyGrowth);
        llBodyChange = findViewById(R.id.llBodyChange);
        llArticle = findViewById(R.id.llArticle);
        llHome = findViewById(R.id.llHome);
    }

    private void fetchBodyChange() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<ArticleList> call = apiService.articleIndex(bearerToken);

        call.enqueue(new Callback<ArticleList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ArticleList> call, Response<ArticleList> response) {
                if(response.isSuccessful()){
                    ArticleList article = response.body();
                    assert article != null;
                    if(article.getSuccess()){
                        dataArrayList.clear();
                        dataArrayList.addAll(article.getData());
                        initializeArticleAdapter();
                        articleAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ArticleActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<ArticleList> call, Throwable t) {

            }
        });
    }

    private void initializeArticleAdapter() {
        articleAdapter = new ArticleAdapter(this, dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener((id -> {
            growthId = id;
        }));

    }

    private void onClickEvents() {
        llBabyGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(babyGrowthIntent);
            }
        });

//        llArticle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(articleIntent);
//            }
//        });

        llBodyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(bodyChangeIntent);
            }
        });

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeIntent);
            }
        });
    }

    private void intents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
        homeIntent = new Intent(this, MainActivity.class);
    }

}