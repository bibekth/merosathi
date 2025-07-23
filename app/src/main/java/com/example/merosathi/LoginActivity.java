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

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etEmail, etPassword;
    String email, password, token, bearerToken;
    TextView tvSignup, tvErrorMessage;
    Intent signupIntent, mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewFinder();
        intents();
        sharedPreference();
        checkToken();
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void viewFinder(){
        tvSignup = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
    }

    protected void intents(){
        signupIntent = new Intent(this, RegisterActivity.class);
        mainIntent = new Intent(this, MainActivity.class);
    }

    protected void clickEvents(){
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signupIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if(email.length() == 0 || password.length() == 0){
                    tvErrorMessage.setText("Please enter your credentails.");
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }else{
                    apiLogin();
                }
            }
        });
    }

    protected void apiLogin() {
        ApiService apiService = RetrofitService.getService(LoginActivity.this).create(ApiService.class);
        Call<User> call = apiService.login(email, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    assert user != null;
                    if(user.getSuccess()){
                        SharedPreferenceManager.saveToken(getApplicationContext(), user.getData().getToken());
                        startActivity(mainIntent);
                    }else{
                        tvErrorMessage.setText("Something went wrong");
                        tvErrorMessage.setVisibility(View.VISIBLE);
                    }
                }else{
                    tvErrorMessage.setText("Credentials did not match.");
                    tvErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvErrorMessage.setText("Something went wrong");
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    protected void textWatcher() {
            etEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
    }

    protected void checkToken() {
        if(token != null){
            startActivity(mainIntent); //This ensures that if there is token saved on the shared preference, then user gets redirected to main activity skipping the login.
        }
    }
}