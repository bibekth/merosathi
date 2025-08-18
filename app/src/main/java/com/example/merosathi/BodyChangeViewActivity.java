package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.merosathi.adapter.ReferenceAdapter;
import com.example.merosathi.model.BodyChange;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BodyChangeViewActivity extends AppCompatActivity {
    Integer id;
    ImageView ivDoctorAvatar, ivBannerImage;
    String token, bearerToken, docContent;
    TextView tvTitle, tvDoctorDescription, tvContent;
    RecyclerView rvReferences;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ReferenceAdapter referenceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_change_view);

        sharedPreference();
        viewFinder();
    }

    @Override
    protected void onResume() {
        super.onResume();

        id = getIntent().getIntExtra("id", 1);
        fetchData();

    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void fetchData() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<BodyChange> call = apiService.bodyChangeShow(bearerToken, id);
        call.enqueue(new Callback<BodyChange>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BodyChange> call, Response<BodyChange> response) {
                if(response.isSuccessful()) {
                    BodyChange bodyChange = response.body();
                    assert bodyChange != null;
                    tvTitle.setText(bodyChange.getData().getTitle());
                    tvContent.setText(bodyChange.getData().getDescription());
                    if(bodyChange.getData().getBanner_image() != null) {
                        Glide.with(getApplicationContext())
                                .load(SharedPreferenceManager.getUrl() + bodyChange.getData().getBanner_image())
                                .into(ivBannerImage);
                    }
                    docContent = bodyChange.getData().getUser().getDoctor().getName() + " | " + bodyChange.getData().getUser().getDoctor().getDescription();
                    tvDoctorDescription.setText(docContent);

                    stringArrayList.clear();
                    if(bodyChange.getData().getReferences() != null) {
                        stringArrayList.addAll(bodyChange.getData().getReferences());
                    }
                    setReferenceAdapter();
                    referenceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BodyChange> call, Throwable t) {

            }
        });
    }

    private void viewFinder() {
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        ivBannerImage = findViewById(R.id.ivBannerImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        tvDoctorDescription = findViewById(R.id.tvDoctorDescription);
        rvReferences = findViewById(R.id.rvReferences);
    }

    private void setReferenceAdapter() {
        referenceAdapter = new ReferenceAdapter(this, stringArrayList);
        rvReferences.setLayoutManager(new LinearLayoutManager(this));
        rvReferences.setAdapter(referenceAdapter);
    }
}