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
    Intent babyGrowthIntent, bodyChangeIntent, articleIntent, profileIntent, notificationIntent, intentFeature;
    List<DateItem> dates = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView weekRecyclerView, rvSquares, rvFeatures;
    WeekAdapter weekAdapter;
    TextView monthYearTextView, tvWeeks;
    LinearLayout llBabyGrowth, llBodyChange, llArticle, llFeatureOne, llFeatureTwo, llFeatureThree, llFeatureFour;
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
        rvFeatures = findViewById(R.id.rvFeatures);
        llFeatureOne = findViewById(R.id.llFeatureOne);
        llFeatureTwo = findViewById(R.id.llFeatureTwo);
        llFeatureThree = findViewById(R.id.llFeatureThree);
        llFeatureFour = findViewById(R.id.llFeatureFour);
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

        llFeatureOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFeature.putExtra("title", "\uD83E\uDD30 Common Problems and Solutions During Pregnancy");
                intentFeature.putExtra("content", "Pregnancy is a beautiful journey, but it also brings physical and emotional changes that may cause discomfort. Understanding common problems and their safe solutions helps you stay healthy and worry-free.\n" +
                        "\n" +
                        "\n" +
                        "✅ Common Problems and Safe Solutions\n" +
                        "\n" +
                        "Morning Sickness (Nausea & Vomiting)\n" +
                        "\n" +
                        "Cause: Hormonal changes in early pregnancy.\n" +
                        "\n" +
                        "Solution: Eat small, frequent meals; sip ginger tea; avoid strong odors; stay hydrated.\n" +
                        "\n" +
                        "Back Pain\n" +
                        "\n" +
                        "Cause: Growing belly and posture changes.\n" +
                        "\n" +
                        "Solution: Practice good posture, wear supportive shoes, use pillows while sleeping, try gentle stretching or yoga.\n" +
                        "\n" +
                        "Constipation\n" +
                        "\n" +
                        "Cause: Hormonal changes slow digestion.\n" +
                        "\n" +
                        "Solution: Eat fiber-rich foods (fruits, vegetables, whole grains), drink plenty of water, and stay active with light exercise.\n" +
                        "\n" +
                        "Swelling of Feet and Ankles (Edema)\n" +
                        "\n" +
                        "Cause: Fluid retention and pressure on veins.\n" +
                        "\n" +
                        "Solution: Rest with feet elevated, wear comfortable shoes, avoid standing for too long, stay hydrated.\n" +
                        "\n" +
                        "Heartburn\n" +
                        "\n" +
                        "Cause: Hormones relax digestive muscles, allowing acid reflux.\n" +
                        "\n" +
                        "Solution: Eat smaller meals, avoid spicy/fried foods, sit upright after meals, and sleep with your head slightly elevated.\n" +
                        "\n" +
                        "Tiredness and Fatigue\n" +
                        "\n" +
                        "Cause: Body working harder, hormonal changes.\n" +
                        "\n" +
                        "Solution: Get enough rest, eat balanced meals, and practice light exercise to boost energy.\n" +
                        "\n" +
                        "Mood Swings\n" +
                        "\n" +
                        "Cause: Hormonal fluctuations and stress.\n" +
                        "\n" +
                        "Solution: Talk with loved ones, practice relaxation techniques like meditation or deep breathing, and seek support if needed.\n" +
                        "\n" +
                        "\n" +
                        "⚠\uFE0F When to See a Doctor\n" +
                        "\n" +
                        "Severe abdominal pain\n" +
                        "\n" +
                        "Heavy bleeding\n" +
                        "\n" +
                        "Severe headache or vision problems\n" +
                        "\n" +
                        "Sudden swelling of face/hands\n" +
                        "\n" +
                        "Reduced baby movements\n" +
                        "\n" +
                        "\uD83D\uDC49 Always consult your doctor or health worker if you experience these symptoms.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCD6 References\n" +
                        "\n" +
                        "World Health Organization (WHO). Pregnancy care and common concerns. (2023). https://www.who.int\n" +
                        "\n" +
                        "American Pregnancy Association. Common Pregnancy Discomforts. (2022). https://americanpregnancy.org");
                intentFeature.putExtra("image", "feature_one");
                startActivity(intentFeature);
            }
        });

        llFeatureTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFeature.putExtra("title", "\uD83E\uDD66 Diet and Nutrition During Pregnancy");
                intentFeature.putExtra("content", "A healthy diet during pregnancy is essential for the growth and development of your baby and for maintaining your own health. Eating the right balance of nutrients can reduce pregnancy complications, boost your energy, and support your baby’s brain, bones, and immune system.\n" +
                        "\n" +
                        "\n" +
                        "✅ Key Nutrients for Pregnancy\n" +
                        "\n" +
                        "Folic Acid (Vitamin B9)\n" +
                        "\n" +
                        "Prevents birth defects of the brain and spine.\n" +
                        "\n" +
                        "Found in: green leafy vegetables, beans, citrus fruits, fortified cereals.\n" +
                        "\n" +
                        "Iron\n" +
                        "\n" +
                        "Helps make more blood to supply oxygen to your baby.\n" +
                        "\n" +
                        "Found in: red meat, chicken, fish, lentils, spinach, and iron-fortified foods.\n" +
                        "\n" +
                        "Tip: Take with Vitamin C-rich foods (like oranges) to improve absorption.\n" +
                        "\n" +
                        "Calcium & Vitamin D\n" +
                        "\n" +
                        "Important for strong bones and teeth for both mother and baby.\n" +
                        "\n" +
                        "Found in: milk, yogurt, cheese, leafy greens, fortified soy products, sunlight (Vitamin D).\n" +
                        "\n" +
                        "Protein\n" +
                        "\n" +
                        "Builds baby’s tissues and organs.\n" +
                        "\n" +
                        "Found in: eggs, fish, lean meats, beans, nuts, and dairy products.\n" +
                        "\n" +
                        "Omega-3 Fatty Acids (DHA, EPA)\n" +
                        "\n" +
                        "Helps baby’s brain and eye development.\n" +
                        "\n" +
                        "Found in: fatty fish (salmon, sardines), walnuts, flaxseeds.\n" +
                        "\n" +
                        "\n" +
                        "\uD83C\uDF4E What to Eat\n" +
                        "\n" +
                        "A variety of fruits and vegetables (at least 5 servings a day).\n" +
                        "\n" +
                        "Whole grains like brown rice, oats, and whole-wheat bread.\n" +
                        "\n" +
                        "Dairy or calcium-rich alternatives (2–3 servings daily).\n" +
                        "\n" +
                        "Healthy snacks like nuts, seeds, and fruits.\n" +
                        "\n" +
                        "Plenty of safe drinking water (8–10 glasses daily).\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDEAB Foods to Limit or Avoid\n" +
                        "\n" +
                        "Raw or undercooked meat, fish, or eggs (risk of infection).\n" +
                        "\n" +
                        "Unpasteurized milk and cheeses.\n" +
                        "\n" +
                        "Too much caffeine (limit to 200mg/day, about 1–2 cups of coffee).\n" +
                        "\n" +
                        "Alcohol (should be avoided completely).\n" +
                        "\n" +
                        "High-mercury fish like shark, swordfish, king mackerel.\n" +
                        "\n" +
                        "\n" +
                        "\uD83C\uDF3F Healthy Eating Tips\n" +
                        "\n" +
                        "Eat small, frequent meals to manage nausea.\n" +
                        "\n" +
                        "Keep a balanced plate: half vegetables & fruits, one-quarter protein, one-quarter whole grains.\n" +
                        "\n" +
                        "Take prenatal vitamins as prescribed by your doctor.\n" +
                        "\n" +
                        "Practice safe food hygiene to prevent infections.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCD6 References\n" +
                        "\n" +
                        "World Health Organization (WHO). Healthy diet during pregnancy. (2023). https://www.who.int\n" +
                        "\n" +
                        "American College of Obstetricians and Gynecologists (ACOG). Nutrition During Pregnancy. (2022). https://www.acog.org");
                intentFeature.putExtra("image", "feature_two");
                startActivity(intentFeature);

            }
        });

        llFeatureThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFeature.putExtra("title","\uD83C\uDFE5 When to Visit the Doctor During Pregnancy (ANC Schedule in Nepal)");
                intentFeature.putExtra("content", "Regular check-ups during pregnancy are called Antenatal Care (ANC) visits. These visits help ensure the health of both the mother and the baby. Health workers check your growth, baby’s heartbeat, provide vaccines, iron tablets, and give important advice.\n" +
                        "\n" +
                        "In Nepal, it is recommended to visit the health facility at least 8 times during pregnancy.\n" +
                        "\n" +
                        "\n" +
                        "✅ Recommended ANC Visit Schedule\n" +
                        "\n" +
                        "First Visit: Within 12 weeks\n" +
                        "\n" +
                        "Confirm pregnancy, check overall health, start iron/folic acid tablets.\n" +
                        "\n" +
                        "Second Visit: At 16 weeks\n" +
                        "\n" +
                        "TT vaccine (first dose), monitor blood pressure and weight.\n" +
                        "\n" +
                        "Third Visit: Between 20–24 weeks\n" +
                        "\n" +
                        "Check baby’s heartbeat, growth, and provide health advice.\n" +
                        "\n" +
                        "Fourth Visit: At 28 weeks\n" +
                        "\n" +
                        "TT vaccine (second dose), continue iron tablets, screen for complications.\n" +
                        "\n" +
                        "Fifth Visit: Between 30–32 weeks\n" +
                        "\n" +
                        "Monitor baby’s movement, growth, and mother’s blood pressure.\n" +
                        "\n" +
                        "Sixth Visit: Between 34–36 weeks\n" +
                        "\n" +
                        "Prepare for safe delivery, check baby’s position, discuss danger signs.\n" +
                        "\n" +
                        "Seventh Visit: Between 37–38 weeks\n" +
                        "\n" +
                        "Final health check-up before delivery, monitor signs of labor.\n" +
                        "\n" +
                        "Eighth Visit: Between 38–40 weeks\n" +
                        "\n" +
                        "Ensure readiness for delivery, check baby’s health, emergency plan if needed.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "⚠\uFE0F Why These Visits Are Important\n" +
                        "\n" +
                        "Early detection of complications (like high blood pressure, anemia).\n" +
                        "\n" +
                        "Safe vaccination (TT to prevent tetanus).\n" +
                        "\n" +
                        "Guidance on nutrition, exercise, and mental health.\n" +
                        "\n" +
                        "Preparation for safe delivery and emergency plans.\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCD6 References\n" +
                        "\n" +
                        "Ministry of Health and Population, Nepal – Safe Motherhood and Newborn Health Program (2023).\n" +
                        "\n" +
                        "World Health Organization (WHO). Antenatal Care Guidelines. (2023). https://www.who.int");
                intentFeature.putExtra("image", "feature_three");
                startActivity(intentFeature);
            }
        });

        llFeatureFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFeature.putExtra("title", "\uD83E\uDDD8\u200D♀\uFE0F Yoga and Exercise During Pregnancy");
                intentFeature.putExtra("content", "Staying active during pregnancy is one of the best ways to support your health and your baby’s development. Safe exercise, including pregnancy-friendly yoga, can improve energy, reduce discomfort, and prepare your body for childbirth.\n" +
                        "\n" +
                        "\n" +
                        "✅ Benefits of Exercise During Pregnancy\n" +
                        "\n" +
                        "Boosts Energy & Reduces Fatigue\n" +
                        "\n" +
                        "Light exercise keeps your body active and improves circulation.\n" +
                        "\n" +
                        "Supports Healthy Weight Gain\n" +
                        "\n" +
                        "Helps manage pregnancy weight safely.\n" +
                        "\n" +
                        "Reduces Common Pregnancy Discomforts\n" +
                        "\n" +
                        "Eases back pain, swelling, constipation, and bloating.\n" +
                        "\n" +
                        "Improves Mood & Sleep\n" +
                        "\n" +
                        "Exercise releases endorphins that reduce stress and anxiety.\n" +
                        "\n" +
                        "Prepares for Labor & Delivery\n" +
                        "\n" +
                        "Strengthens muscles, improves flexibility, and boosts stamina.\n" +
                        "\n" +
                        "\n" +
                        "\uD83E\uDDD8\u200D♀\uFE0F Safe Yoga & Exercises\n" +
                        "\n" +
                        "Pregnancy-Safe Yoga Poses:\n" +
                        "\n" +
                        "Cat-Cow Stretch (for back pain relief)\n" +
                        "\n" +
                        "Butterfly Pose (for hip flexibility)\n" +
                        "\n" +
                        "Side-Lying Stretch (for relaxation)\n" +
                        "\n" +
                        "Easy Breathing & Meditation (to calm mind and body)\n" +
                        "\n" +
                        "Other Gentle Exercises:\n" +
                        "\n" +
                        "Walking (20–30 mins daily)\n" +
                        "\n" +
                        "Swimming (low-impact, full-body exercise)\n" +
                        "\n" +
                        "Pelvic floor (Kegel) exercises\n" +
                        "\n" +
                        "Light stretching\n" +
                        "\n" +
                        "\n" +
                        "⚠\uFE0F Safety Tips\n" +
                        "\n" +
                        "Always consult your doctor before starting new exercises.\n" +
                        "\n" +
                        "Avoid high-impact workouts or those with a risk of falling.\n" +
                        "\n" +
                        "Do not exercise on your back for long after the first trimester.\n" +
                        "\n" +
                        "Stay hydrated and avoid overheating.\n" +
                        "\n" +
                        "Listen to your body — stop if you feel dizzy, short of breath, or in pain.\n" +
                        "\n" +
                        "\n" +
                        "\uD83D\uDCD6 References\n" +
                        "\n" +
                        "American College of Obstetricians and Gynecologists (ACOG). Exercise During Pregnancy. (2022). https://www.acog.org\n" +
                        "\n" +
                        "Mayo Clinic. Pregnancy and exercise: Baby, let’s move! (2023). https://www.mayoclinic.org");
                intentFeature.putExtra("image", "feature_four");
                startActivity(intentFeature);
            }
        });
    }

    private void intents() {
        babyGrowthIntent = new Intent(this, BabyGrowthActivity.class);
        bodyChangeIntent = new Intent(this, BodyChangeActivity.class);
        articleIntent = new Intent(this, ArticleActivity.class);
        profileIntent = new Intent(this, ProfileActivity.class);
        notificationIntent = new Intent(this, NotificationActivity.class);
        intentFeature = new Intent(this, FeatureActivity.class);
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