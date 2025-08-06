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

import com.example.merosathi.adapter.BabyGrowthAdapter;
import com.example.merosathi.model.BabyGrowth;
import com.example.merosathi.model.BabyGrowthList;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BabyGrowthActivity extends AppCompatActivity {
    BabyGrowthAdapter babyGrowthAdapter;
    ArrayList<BabyGrowthList.Data> dataArrayList = new ArrayList<>();
    Integer growthId;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, homeIntent, babyGrowthViewIntent;
    RecyclerView recyclerView;
    String token, bearerToken;
    LinearLayout llBabyGrowth, llBodyChange, llArticle, llHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_growth);

        sharedPreference();
        viewFinder();
        fetchBabyGrowth();
        intents();
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

    private void fetchBabyGrowth() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<BabyGrowthList> call = apiService.babyGrowthIndex(bearerToken);

        call.enqueue(new Callback<BabyGrowthList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BabyGrowthList> call, Response<BabyGrowthList> response) {
                if(response.isSuccessful()){
                    BabyGrowthList babyGrowth = response.body();
                    assert babyGrowth != null;
                    if(babyGrowth.getSuccess()){
                        dataArrayList.clear();
                        dataArrayList.addAll(babyGrowth.getData());
                        initializeBabyGrowthAdapter();
                        babyGrowthAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(BabyGrowthActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<BabyGrowthList> call, Throwable t) {

            }
        });
    }

    private void initializeBabyGrowthAdapter() {
        babyGrowthAdapter = new BabyGrowthAdapter(this, dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(babyGrowthAdapter);
        babyGrowthAdapter.setOnItemClickListener((id -> {
            babyGrowthViewIntent.putExtra("id", id);
            startActivity(babyGrowthViewIntent);
        }));

    }

    private void onClickEvents() {
//        llBabyGrowth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(babyGrowthIntent);
//            }
//        });

        llArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(articleIntent);
            }
        });

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
        babyGrowthViewIntent = new Intent(this, BabyGrowthViewActivity.class);
    }
}