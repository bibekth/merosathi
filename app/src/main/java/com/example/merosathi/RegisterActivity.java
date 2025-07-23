package com.example.merosathi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.merosathi.model.User;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText etName, etUserName, etEmail, etContact;
    ScrollView scrollView;
    String name, username, email, contact, dob = "";
    TextView tvLogin, tvNameError, tvUserNameError, tvEmailError, tvContactError, tvDateOfBirthError, tvDateOfBirth;
    Intent loginIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewFinder();
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

        clickEvents();
        textWatcher();
        limitation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void viewFinder(){
        tvLogin = findViewById(R.id.tvLogin);
        etName = findViewById(R.id.etName);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        btnRegister = findViewById(R.id.btnRegister);
        tvNameError = findViewById(R.id.tvNameError);
        tvUserNameError = findViewById(R.id.tvUserNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvContactError = findViewById(R.id.tvContactError);
        tvDateOfBirthError = findViewById(R.id.tvDateOfBirthError);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        scrollView = findViewById(R.id.scrollView);
    }

    protected void intents(){
        loginIntent = new Intent(this, LoginActivity.class);
    }

    protected void clickEvents(){
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringFromEditText();

                Boolean hasError = false;

                if(Objects.equals(name, "")){
                    tvNameError.setText("Name is required");
                    tvNameError.setVisibility(View.VISIBLE);
                    hasError = true;
                }

                if(Objects.equals(username, "")){
                    tvUserNameError.setText("Username is required");
                    tvUserNameError.setVisibility(View.VISIBLE);
                    hasError = true;
                }

                if(Objects.equals(email, "")){
                    tvEmailError.setText("Email is required");
                    tvEmailError.setVisibility(View.VISIBLE);
                    hasError = true;
                }

                if(Objects.equals(contact, "")){
                    tvContactError.setText("Contact in required");
                    tvContactError.setVisibility(View.VISIBLE);
                    hasError = true;
                }

                if(Objects.equals(dob, "")){
                    tvDateOfBirthError.setText("Date of birth is required");
                    tvDateOfBirthError.setVisibility(View.VISIBLE);
                    hasError = true;
                }

                if(!hasError) {
                    apiRegister();
                }
            }
        });

        tvDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDateOfBirthError.getVisibility() == View.VISIBLE){
                    tvDateOfBirthError.setVisibility(View.GONE);
                }

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH); // 0-based
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create and show DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                                dob = selectedDate;
                                tvDateOfBirth.setText(selectedDate);
                            }
                        },
                        year, month, day
                );

                datePickerDialog.show();
            }
        });
    }

    protected void apiRegister(){
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<User> call = apiService.register(name, username, email, contact, dob);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    assert user != null;
                    if(user.getSuccess()){
                        startActivity(loginIntent);
                    }else{

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    protected void stringFromEditText(){
        name = etName.getText().toString();
        username = etUserName.getText().toString();
        email = etEmail.getText().toString();
        contact = etContact.getText().toString();
    }

    protected void textWatcher(){
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tvNameError.getVisibility() == View.VISIBLE){
                    tvNameError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tvUserNameError.getVisibility() == View.VISIBLE){
                    tvUserNameError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tvEmailError.getVisibility() == View.VISIBLE){
                    tvEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tvContactError.getVisibility() == View.VISIBLE){
                    tvContactError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void limitation() {
        etContact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
    }
}