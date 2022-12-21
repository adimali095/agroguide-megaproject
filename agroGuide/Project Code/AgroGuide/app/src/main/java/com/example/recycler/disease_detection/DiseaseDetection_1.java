package com.example.recycler.disease_detection;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycler.R;

public class DiseaseDetection_1 extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] s1, s2;
    // int images[]={R.drawable.dd1_corn,R.drawable.dd1_tomato,R.drawable.dd1_potato,R.drawable.dd1_soybean,R.drawable.dd1_sugarcane,R.drawable.dd1_flower};
    int[] images = {
            R.drawable.dd1_corn,
            R.drawable.dd1_tomato,
            R.drawable.dd1_potato,
            R.drawable.dd1_apple,
            R.drawable.dd1_cherry,
            R.drawable.dd1_grape
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detection1);

        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.plants_name);
        s2 = getResources().getStringArray(R.array.description);

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}