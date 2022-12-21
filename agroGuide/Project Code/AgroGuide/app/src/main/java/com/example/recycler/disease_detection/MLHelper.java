package com.example.recycler.disease_detection;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.recycler.ml.AppleModelUnquant;
import com.example.recycler.ml.CherryModelUnquant;
import com.example.recycler.ml.CornModelUnquant;
import com.example.recycler.ml.GrapeModelUnquant;
import com.example.recycler.ml.PotatoModelUnquant;
import com.example.recycler.ml.TomatoModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MLHelper {
    private final static String corn = "Corn";
    private final static String tomato = "Tomato";
    private final static String potato = "Potato";
    private final static String apple = "Apple";
    private final static String cherry = "Cherry";
    private final static String grape = "Grape";
    private final Context context;
    private final String cropName;
    private Bitmap cropImage;
    private String detectedDisease;

    MLHelper(Context context, Bitmap cropImage, String cropName) {
        this.context = context;
        this.cropImage = cropImage;
        this.cropName = cropName;
    }

    public void detectDisease() {
        switch (cropName) {
            case corn:
                detectedDisease = cornDiseaseDetect();
                break;
            case tomato:
                detectedDisease = tomatoDiseaseDetect();
                break;
            case potato:
                detectedDisease = potatoDiseaseDetect();
                break;
            case apple:
                detectedDisease = appleDiseaseDetect();
                break;
            case cherry:
                detectedDisease = cherryDiseaseDetect();
                break;
            case grape:
                detectedDisease = grapeDiseaseDetect();
                break;
        }
    }

    public String getDetectedDisease() {
        return detectedDisease;
    }

    public String cornDiseaseDetect() {
        String disease = "temp";

        final String[] cornClasses =
                {
                        "Corn_(maize)___healthy",
                        "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot",
                        "Corn_(maize)___Common_rust_",
                        "Corn_(maize)___Northern_Leaf_Blight",
                        "Corn_(maize)___Not_Corn"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            CornModelUnquant model = CornModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            CornModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = cornClasses[maxPos];
        } catch (IOException e) {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }

    public String tomatoDiseaseDetect() {
        String disease = "temp";

        final String[] tomatoClasses =
                {
                        "Tomato___Bacterial_spot",
                        "Tomato___Early_blight",
                        "Tomato___healthy",
                        "Tomato___Late_blight",
                        "Tomato___Leaf_Mold",
                        "Tomato___not_tomato"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            TomatoModelUnquant model = TomatoModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            TomatoModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = tomatoClasses[maxPos];
        } catch (IOException e) {
            //
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }

    public String potatoDiseaseDetect() {
        String disease = "temp";

        final String[] potatoClasses =
                {
                        "Potato___Early_blight",
                        "Potato___healthy",
                        "Potato___Late_blight",
                        "Potato___not_potato"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            PotatoModelUnquant model = PotatoModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            PotatoModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = potatoClasses[maxPos];
        } catch (IOException e) {
            //
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }

    public String appleDiseaseDetect() {
        String disease = "temp";

        final String[] appleClasses =
                {
                        "Apple___Apple_scab",
                        "Apple___Black_rot",
                        "Apple___Cedar_apple_rust",
                        "Apple___healthy",
                        "Apple___not_apple"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            AppleModelUnquant model = AppleModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            AppleModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = appleClasses[maxPos];
        } catch (IOException e) {
            //
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }

    public String cherryDiseaseDetect() {
        String disease = "temp";

        final String[] cherryClasses =
                {
                        "Cherry_(including_sour)___healthy",
                        "Cherry_(including_sour)___Powdery_mildew",
                        "Cherry__not_cherry"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            CherryModelUnquant model = CherryModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            CherryModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = cherryClasses[maxPos];
        } catch (IOException e) {
            //
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }

    public String grapeDiseaseDetect() {
        String disease = "temp";

        final String[] grapeClasses =
                {
                        "Grape___Black_rot",
                        "Grape___Esca_(Black_Measles)",
                        "Grape___healthy",
                        "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
                        "Grape___not_grape"
                };
        cropImage = Bitmap.createScaledBitmap(cropImage, 224, 224, true);
        try {

            GrapeModelUnquant model = GrapeModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            this.cropImage.getPixels(intValues, 0, this.cropImage.getWidth(), 0, 0, this.cropImage.getWidth(), this.cropImage.getHeight());
            int pixel = 0;
            for (int i = 0; i < 224; i++) {
                for (int j = 0; j < 224; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            GrapeModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            // Releases model resources if no longer used.
            model.close();
            disease = grapeClasses[maxPos];
        } catch (IOException e) {
            //
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
        }
        return disease;
    }
}
