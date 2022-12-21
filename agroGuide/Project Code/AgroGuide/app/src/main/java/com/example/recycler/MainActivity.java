package com.example.recycler;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //variables
    private static final int SPLASH_SCREEN = 5000;
    Animation topAnim, bottomAnim;
    ImageView ivFarmer;
    TextView tvAppname, tvTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //connecting gui
        ivFarmer = findViewById(R.id.ivFarmer);
        tvAppname = findViewById(R.id.tvAppname);
        tvTagline = findViewById(R.id.tvTagline);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //setting animation
        ivFarmer.setAnimation(topAnim);
        tvAppname.setAnimation(bottomAnim);
        tvTagline.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, Login.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(ivFarmer, "logo_image");
                pairs[1] = new Pair<View, String>(tvAppname, "logo_text");
                pairs[2] = new Pair<View, String>(tvTagline, "logo_tagline");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);

                startActivity(intent, options.toBundle());
                finish();

                /*
                Intent intent = new Intent(MainActivity.this, Login.class);
                //Intent intent = new Intent(MainActivity.this,DiseaseDetection_1.class);
                startActivity(intent);
                finish();

                */
            }
        }, SPLASH_SCREEN);
    }
}