package com.example.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.recycler.weather_details.WeatherDetails;
import com.example.newsworld.NewsWorld;
import com.example.recycler.disease_detection.DiseaseDetection_1;
import com.example.market_price.MarketPriceActivity;
import com.example.todolist.ToDoActivity;
import com.example.recycler.fertilizer_calculator.FertilizerCalculator;

public class Dashboard extends AppCompatActivity
{
    ImageView profileView;
    LinearLayout DiseaseDetectionView,WeatherView,NewsView,MarketPriceView,ToDoView;
    LinearLayout FertilizerCal;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profileView = findViewById(R.id.profile_view);
        WeatherView = findViewById(R.id.weather_view);
        NewsView = findViewById(R.id.news_view);
        DiseaseDetectionView = findViewById(R.id.detection_view);
        MarketPriceView = findViewById(R.id.market_price_view);
        ToDoView = findViewById(R.id.todo_view);
        FertilizerCal = findViewById(R.id.fertilizer_calculator);

        //Location request for weather
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callProfile(view);
            }
        });

        WeatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callWeatherDetails(view);
            }
        });

        NewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNews(view);
            }
        });

        DiseaseDetectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callDiseaseDetection(view);
            }
        });

        MarketPriceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMarketPrice(view);
            }
        });

        ToDoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callToDoList(view);
            }
        });

        FertilizerCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callFertilizerCalculator(view);
            }
        });

    }

    public void callProfile(View view) {
        String logUsername = getIntent().getStringExtra("username");
        String logEmail = getIntent().getStringExtra("email");
        String logPhoneNo = getIntent().getStringExtra("phoneNo");
        String logFullname = getIntent().getStringExtra("fullname");
        String logPasswrod = getIntent().getStringExtra("password");

        Intent intent = new Intent(getApplicationContext(),UserProfile.class);
        intent.putExtra("fullname",logFullname);
        intent.putExtra("username",logUsername);
        intent.putExtra("email",logEmail);
        intent.putExtra("password",logPasswrod);
        intent.putExtra("phoneNo",logPhoneNo);

        startActivity(intent);
    }
    public void callWeatherDetails(View view) {
        Intent intent = new Intent(Dashboard.this, WeatherDetails.class);
        startActivity(intent);
    }
    public void callNews(View view) {
        Intent intent = new Intent(Dashboard.this, NewsWorld.class);
        startActivity(intent);
    }
    public void callDiseaseDetection(View view) {
        Intent intent = new Intent(Dashboard.this, DiseaseDetection_1.class);
        startActivity(intent);
    }
    public void callMarketPrice(View view) {
        Intent intent = new Intent(Dashboard.this, MarketPriceActivity.class);
        startActivity(intent);
    }
    public void callToDoList(View view) {
        Intent intent = new Intent(Dashboard.this, ToDoActivity.class);
        startActivity(intent);
    }
    public void callFertilizerCalculator(View view) {
        Intent intent = new Intent(Dashboard.this, FertilizerCalculator.class);
        startActivity(intent);
    }
}