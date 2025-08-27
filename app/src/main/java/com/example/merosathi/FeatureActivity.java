package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FeatureActivity extends AppCompatActivity {
    String title, content, image;
    TextView tvTitle, tvContent;
    ImageView ivBannerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        image = getIntent().getStringExtra("image");

        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        ivBannerImage = findViewById(R.id.ivBannerImage);

        tvTitle.setText(title);
        tvContent.setText(content);
        int resId = getResources().getIdentifier(image, "drawable", getPackageName());
//        ivBannerImage.setImageResource(resId);
        Glide.with(this)
                .load(resId)
                .centerCrop()
                .into(ivBannerImage);
    }
}