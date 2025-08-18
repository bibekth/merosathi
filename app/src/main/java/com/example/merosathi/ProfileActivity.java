package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merosathi.model.User;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ImageView profileImage, btn_back;
    TextView userName, userEmail, userPhone, expected_date, dob;
    Button btnEditProfile;
    String token, bearerToken;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewFinder();
        sharedPreference();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUserProfile();
        intents();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void viewFinder() {
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        expected_date = findViewById(R.id.expected_date);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        dob = findViewById(R.id.dob);
        btn_back = findViewById(R.id.btn_back);
    }

    private void intents() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("name", userName.getText());
            intent.putExtra("email", userEmail.getText());
            intent.putExtra("contact", userPhone.getText());
            intent.putExtra("dob", dob.getText());
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<User> call = apiService.profile(bearerToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    userName.setText(user.getData().getPerson().getName());
                    userEmail.setText(user.getData().getEmail());
                    userPhone.setText(user.getData().getPerson().getContact());
                    expected_date.setText(user.getData().getPerson().getExpected_date());
                    dob.setText(user.getData().getPerson().getDob());
                }else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }
}