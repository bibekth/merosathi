package com.example.merosathi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.merosathi.adapter.SquareMainAdapter;
import com.example.merosathi.adapter.WeekAdapter;
import com.example.merosathi.model.MainModel;
import com.example.merosathi.model.SquareItem;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Calendar currentWeekStart;
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, profileIntent, notificationIntent;
    List<DateItem> dates = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView weekRecyclerView, rvSquares;
    WeekAdapter weekAdapter;
    TextView monthYearTextView, tvWeeks;
    LinearLayout llBabyGrowth, llBodyChange, llArticle;
    ImageView ivProfileIcon, ivBabyGrowthImage, ivNotification;
    String token, bearerToken, date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    List<SquareItem> squareItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFinder();
        intents();
        horizontalRecycleHandler();
        onClickEvents();
        sharedPreference();
        makeAPICall();
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
        generateDates(currentWeekStart, -365, 365);

        weekAdapter = new WeekAdapter(dates);
        weekRecyclerView.setAdapter(weekAdapter);

        // Set current day as selected
        Calendar today = Calendar.getInstance();
        weekAdapter.setSelectedDate(today);

        // Scroll to the current week
        scrollToCurrentWeek();

        weekAdapter.setOnDateSelectedListener((dateItem, position) -> {
            // Handle date selection
            date = dateItem.getYear() + "-" + (dateItem.getMonth() + 1) + "-" + dateItem.getDayOfMonth();
            updateMonthYearHeader(dateItem.toCalendar());
            makeAPICall();
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
        tempCalendar = (Calendar) startDate.clone();
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
        ivProfileIcon = findViewById(R.id.ivProfileIcon);
        tvWeeks = findViewById(R.id.tvWeeks);
        ivBabyGrowthImage = findViewById(R.id.ivBabyGrowthImage);
        rvSquares = findViewById(R.id.rvSquares);
        ivNotification = findViewById(R.id.ivNotification);
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

        ivProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(profileIntent);
            }
        });

        ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(notificationIntent);
            }
        });
    }

    private void intents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        notificationIntent = new Intent(this, NotificationActivity.class);
    }

    private void makeAPICall()
    {
        ApiService apiService = RetrofitService.getService(this).create(ApiService.class);
        Call<MainModel> call = apiService.main(bearerToken, date);
        call.enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.isSuccessful()) {
                    MainModel mainModel = response.body();

                    assert mainModel != null;

                    tvWeeks.setText(mainModel.getData().getTime());

                    if(mainModel.getData().getBaby_growth() != null) {
                        String imageUrl = mainModel.getData().getBaby_growth().getBanner_image();

                        assert imageUrl != null;

                        Glide.with(MainActivity.this)
                                .load(SharedPreferenceManager.getUrl() + imageUrl)
                                .into(ivBabyGrowthImage);
                    }

                    squareItems.clear();
                    
                    if (mainModel.getData().getBody_change() != null) {
                        squareItems.add(new SquareItem(
                                mainModel.getData().getBody_change().getId(),
                                mainModel.getData().getBody_change().getTitle(),
                                mainModel.getData().getBody_change().getBanner_image(),
                                "baby_growth"
                        ));
                    }

                    if (mainModel.getData().getBaby_growth() != null) {
                        squareItems.add(new SquareItem(
                                mainModel.getData().getBaby_growth().getId(),
                                mainModel.getData().getBaby_growth().getTitle(),
                                mainModel.getData().getBaby_growth().getBanner_image(),
                                "body_change"
                        ));
                    }

                    squareBoxesAdapter();
                }
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {

            }
        });
    }

    private void sharedPreference() {
        token = SharedPreferenceManager.getToken(getApplicationContext());
        bearerToken = SharedPreferenceManager.getBearerToken(getApplicationContext());
    }

    private void squareBoxesAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSquares.setLayoutManager(layoutManager);
        SquareMainAdapter adapter = new SquareMainAdapter(this, squareItems, item -> {
            Intent intent;
            if (item.getType().equals("baby_growth")) {
                intent = new Intent(MainActivity.this, BabyGrowthViewActivity.class);
            } else if (item.getType().equals("body_change")) {
                intent = new Intent(MainActivity.this, BodyChangeViewActivity.class);
            } else {
                return; // Unknown type, do nothing
            }

            // Pass the id as an extra
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });
//        SquareMainAdapter adapter = new SquareMainAdapter(this, squareItems);
        rvSquares.setAdapter(adapter);

    }
}