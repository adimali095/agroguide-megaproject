package com.example.recycler.disease_detection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.recycler.R;

import java.io.IOException;

public class DiseaseDetection_2 extends AppCompatActivity {
    TextView tvTitle;
    String cropName;
    Button btnStart, btnSelect;
    ImageView iv1;
    Bitmap img;
    Uri toPassUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detection2);

        cropName = getIntent().getStringExtra("title");

        btnStart = findViewById(R.id.btnStart);

        btnSelect = findViewById(R.id.btnSelect);
        iv1 = findViewById(R.id.iv1);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(cropName);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toPassUri == null) {
                    Toast.makeText(DiseaseDetection_2.this, "Please select an Image !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DiseaseDetection_2.this, DiseaseDetection_3.class);
                    //intent.putExtra("image",img);
                    intent.putExtra("imageUri", toPassUri.toString());
                    intent.putExtra("cropName", cropName);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(DiseaseDetection_2.this, iv1, ViewCompat.getTransitionName(iv1));
                    startActivity(intent, options.toBundle());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            assert data != null;
            iv1.setImageURI(data.getData());
            Uri uri = data.getData();
            toPassUri = uri;
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}