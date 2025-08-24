package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowDeliveryDateActivity extends AppCompatActivity {

    private TextView tvMessage;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_delivery_date);

        tvMessage = findViewById(R.id.tvMessage);
        btnDone = findViewById(R.id.btnDone);

        String date = getIntent().getStringExtra("date");
        tvMessage.setText("You will meet your baby around " + date);

        btnDone.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
