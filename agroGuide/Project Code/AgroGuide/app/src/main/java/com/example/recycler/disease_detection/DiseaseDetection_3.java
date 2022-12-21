package com.example.recycler.disease_detection;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recycler.R;

import java.io.IOException;

public class DiseaseDetection_3 extends AppCompatActivity {
    Bitmap bitmapCropImage;
    String stringCropName;
    ImageView ivCropImage;
    Uri cropImageUri;
    TextView tvDisease, tvDiseaseDescription, tvStepsNeeded, tvActualSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detection3);


        ivCropImage = findViewById(R.id.ivCropImage);

        //views to pass to DiseaseDescHelper class
        tvDisease = findViewById(R.id.tvDisease);
        tvDiseaseDescription = findViewById(R.id.tvDiseaseDescription);
        tvStepsNeeded = findViewById(R.id.tvStepsNeeded);
        tvActualSteps = findViewById(R.id.tvActualSteps);
        //end

        //img = (Bitmap) getIntent().getParcelableExtra("image");
        cropImageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        stringCropName = getIntent().getStringExtra("cropName");
        try {
            bitmapCropImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivCropImage.setImageBitmap(bitmapCropImage);

        detectDisease();
    }

    public void detectDisease() {
        String diseaseName;

        MLHelper mlHelper = new MLHelper(getApplicationContext(), bitmapCropImage, stringCropName);
        mlHelper.detectDisease();
        diseaseName = mlHelper.getDetectedDisease();

        DiseaseDescHelper diseaseDescHelper = new DiseaseDescHelper(diseaseName, tvDisease, tvDiseaseDescription, tvStepsNeeded, tvActualSteps);
        //DiseaseDescHelper diseaseDescHelper = new DiseaseDescHelper("disease_name1",tvDisease,tvDiseaseDescription,tvStepsNeeded,tvActualSteps);

        diseaseDescHelper.getDiseaseDescriptionSteps(getResources().getStringArray(R.array.disease_description_cure));
        //resource array pass kela ahe karn DiseaseDescHelper mdhe getResources() access krta yet nahi

        diseaseDescHelper.setDiseaseDescriptionSteps();
    }
}