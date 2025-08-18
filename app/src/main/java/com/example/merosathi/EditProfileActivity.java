package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.merosathi.model.User;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText inputName, inputEmail, inputPhone, inputDob;
    ImageView profileImage;
    Button btnSave;
    String token, bearerToken, name, email, contact, dob;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        viewFinder();
        sharedPreference();
        preFill();
    }

    @Override
    protected void onResume() {
        super.onResume();

        clickEvents();
    }

    private void viewFinder() {
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        inputDob = findViewById(R.id.input_dob);
        profileImage = findViewById(R.id.edit_profile_image);
        btnSave = findViewById(R.id.btn_save_profile);
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void preFill() {
        Intent intent = getIntent();
        if (intent != null) {
            inputName.setText(intent.getStringExtra("name"));
            inputEmail.setText(intent.getStringExtra("email"));
            inputPhone.setText(intent.getStringExtra("contact"));
            inputDob.setText(intent.getStringExtra("dob"));
        }
    }

    private void clickEvents() {
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        contact = inputPhone.getText().toString().trim();
        dob = inputDob.getText().toString().trim();

//        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
//            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
//            return;
//        }

        apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<User> call = apiService.editProfile(bearerToken, name, contact, email, dob);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();

                    Intent profileIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}