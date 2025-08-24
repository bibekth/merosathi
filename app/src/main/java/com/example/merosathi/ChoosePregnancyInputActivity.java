package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChoosePregnancyInputActivity extends AppCompatActivity {

    private Button btnLMP, btnWeeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pregnancy_input);

        btnLMP = findViewById(R.id.btnLMP);
        btnWeeks = findViewById(R.id.btnWeeks);

        btnLMP.setOnClickListener(v -> startActivity(new Intent(this, EnterLMPActivity.class)));
        btnWeeks.setOnClickListener(v -> startActivity(new Intent(this, EnterWeeksActivity.class)));
    }
}
