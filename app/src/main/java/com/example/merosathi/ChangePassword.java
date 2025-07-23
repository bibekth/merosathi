package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merosathi.model.User;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    Boolean submitBool = true;
    Button btnSubmit;
    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Intent mainIntent;
    String oldPassword, newPassword, confirmPassword, token;
    TextView tvErrorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        viewFinder();
        intents();
        sharedPreferences();
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

        clickEvents();
        textWatcher();
    }

    protected void viewFinder() {
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
    }

    protected void intents() {
        mainIntent = new Intent(this, MainActivity.class);
    }

    protected void clickEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = etOldPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                if(!Objects.equals(newPassword, confirmPassword)) {
                    tvErrorMessage.setText("New password did not match with re-enter password.");
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    submitBool = false;
                }

                if(submitBool){
                    apiChangePassword();
                }
            }
        });
    }

    protected void apiChangePassword() {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<User> call = apiService.changePassword(token, newPassword, oldPassword, confirmPassword);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(response.isSuccessful()){
                    assert user != null;
                    Toast.makeText(ChangePassword.this, "Successful", Toast.LENGTH_SHORT).show();
                }else{
                    tvErrorMessage.setText(user.getMessage());
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvErrorMessage.setText("Something went wrong.");
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void sharedPreferences() {
        token = SharedPreferenceManager.getBearerToken(this);
    }

    protected void textWatcher() {
        if(tvErrorMessage.getVisibility() == View.VISIBLE){
            etOldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tvErrorMessage.setText("");
                    tvErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etNewPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tvErrorMessage.setText("");
                    tvErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etConfirmPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tvErrorMessage.setText("");
                    tvErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}