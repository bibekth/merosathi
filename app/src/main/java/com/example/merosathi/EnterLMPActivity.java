package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterLMPActivity extends AppCompatActivity {

    private EditText etLMP;
    private Button btnSubmit;
    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_lmpactivity);

        etLMP = findViewById(R.id.etLMP);
        btnSubmit = findViewById(R.id.btnSubmitLMP);

        token = SharedPreferenceManager.getToken(this);
        apiService = RetrofitService.getService(this).create(ApiService.class);

        btnSubmit.setOnClickListener(v -> submitLMP());
    }

    private void submitLMP() {
        String lmp = etLMP.getText().toString().trim();
        if (lmp.isEmpty()) {
            Toast.makeText(this, "Enter your LMP", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("lmp", lmp);

        Call<HashMap<String, String>> call = apiService.calculateDay("Bearer " + token, map);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String date = response.body().get("deliver date");
                    Intent intent = new Intent(EnterLMPActivity.this, ShowDeliveryDateActivity.class);
                    intent.putExtra("date", date);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterLMPActivity.this, "Failed to calculate", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Toast.makeText(EnterLMPActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
