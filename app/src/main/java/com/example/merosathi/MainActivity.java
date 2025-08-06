package com.example.merosathi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merosathi.adapter.WeekAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Calendar currentWeekStart;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent;
    List<DateItem> dates = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView weekRecyclerView;
    WeekAdapter weekAdapter;
    TextView monthYearTextView;
    LinearLayout llBabyGrowth, llBodyChange, llArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFinder();
        intents();
        horizontalRecycleHandler();
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

        onClickEvents();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void horizontalRecycleHandler() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weekRecyclerView.setLayoutManager(layoutManager);

        // Initialize with current week
        currentWeekStart = Calendar.getInstance();
        // Set to the start of the current week (e.g., Sunday or Monday based on locale)
        currentWeekStart.set(Calendar.DAY_OF_WEEK, currentWeekStart.getFirstDayOfWeek());

        // Populate initial dates (e.g., a few months around current week)
        generateDates(currentWeekStart, -60, 60); // 60 days before, 60 days after

        weekAdapter = new WeekAdapter(dates);
        weekRecyclerView.setAdapter(weekAdapter);

        // Set current day as selected
        Calendar today = Calendar.getInstance();
        weekAdapter.setSelectedDate(today);

        // Scroll to the current week
        scrollToCurrentWeek();

        weekAdapter.setOnDateSelectedListener((dateItem, position) -> {
            // Handle date selection
            Toast.makeText(MainActivity.this, "Selected: " + dateItem.getDayOfWeek() + ", " + dateItem.getDayOfMonth() + "/" + (dateItem.getMonth() + 1) + "/" + dateItem.getYear(), Toast.LENGTH_SHORT).show();
            updateMonthYearHeader(dateItem.toCalendar());
        });

        // Implement scroll listener for dynamic loading or updating header
        weekRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    DateItem visibleDate = dates.get(firstVisibleItemPosition);
                    updateMonthYearHeader(visibleDate.toCalendar());

                    // Optional: Implement infinite scrolling here by adding more dates
                    // if user scrolls close to the start/end of the current list.
                }
            }
        });

        // Set initial month/year header
        updateMonthYearHeader(today);
    }

    private void generateDates(Calendar startDate, int daysBefore, int daysAfter) {
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", Locale.getDefault());

        // Add dates before
        Calendar tempCalendar = (Calendar) startDate.clone();
        tempCalendar.add(Calendar.DAY_OF_YEAR, daysBefore);
        for (int i = 0; i < Math.abs(daysBefore); i++) {
            dates.add(new DateItem(
                    dayOfWeekFormat.format(tempCalendar.getTime()),
                    dayOfMonthFormat.format(tempCalendar.getTime()),
                    tempCalendar.get(Calendar.YEAR),
                    tempCalendar.get(Calendar.MONTH),
                    tempCalendar.get(Calendar.DAY_OF_MONTH)
            ));
            tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Add dates starting from startDate
        for (int i = 0; i <= daysAfter; i++) {
            dates.add(new DateItem(
                    dayOfWeekFormat.format(tempCalendar.getTime()),
                    dayOfMonthFormat.format(tempCalendar.getTime()),
                    tempCalendar.get(Calendar.YEAR),
                    tempCalendar.get(Calendar.MONTH),
                    tempCalendar.get(Calendar.DAY_OF_MONTH)
            ));
            tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void scrollToCurrentWeek() {
        Calendar today = Calendar.getInstance();
        // Find the position of today's date in the list
        int todayPosition = -1;
        for (int i = 0; i < dates.size(); i++) {
            DateItem item = dates.get(i);
            if (item.getYear() == today.get(Calendar.YEAR) &&
                    item.getMonth() == today.get(Calendar.MONTH) &&
                    item.getDay() == today.get(Calendar.DAY_OF_MONTH)) {
                todayPosition = i;
                break;
            }
        }

        if (todayPosition != -1) {
            // Scroll to the current day, and adjust to show the full week
            // You might need to adjust this offset based on how many days are visible
            int offset = 3; // To center the current day more or less
            layoutManager.scrollToPositionWithOffset(Math.max(0, todayPosition - offset), 0);
        }
    }

    private void updateMonthYearHeader(Calendar calendar) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearTextView.setText(monthYearFormat.format(calendar.getTime()));
    }

    private void viewFinder() {
        weekRecyclerView = findViewById(R.id.weekRecyclerView);
        monthYearTextView = findViewById(R.id.tvCurrentDate);
        llBabyGrowth = findViewById(R.id.llBabyGrowth);
        llBodyChange = findViewById(R.id.llBodyChange);
        llArticle = findViewById(R.id.llArticle);
    }

    private void onClickEvents() {
        llBabyGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(babyGrowthIntent);
            }
        });

        llArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(articleIntent);
            }
        });

        llBodyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(bodyChangeIntent);
            }
        });
    }

    private void intents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
    }

    private void makeAPICall()
    {

    }
}