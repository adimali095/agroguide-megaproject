package com.example.recycler.weather_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.recycler.MainActivity;
import com.example.recycler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherDetails extends AppCompatActivity {

    final String API = "d70cb6edb5784b8eb89014e9a7c06c57";

    final String TAG = MainActivity.class.getSimpleName();

    ProgressBar loader;
    RelativeLayout mainContainer;
    TextView errorText;
    TextView iAddress, iUpdated_at, iStatus, iTemp, iTemp_min, iTemp_max, iSunrise, iSunset, iWind, iPressure, iHumidity;

    String latitude, longitude;

    WeatherGpsTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        loader = findViewById(R.id.loader);
        mainContainer = findViewById(R.id.mainContainer);
        errorText = findViewById(R.id.errorText);

        iAddress = findViewById(R.id.address);
        iUpdated_at = findViewById(R.id.updated_at);
        iStatus = findViewById(R.id.status);
        iTemp = findViewById(R.id.temp);
        iTemp_min = findViewById(R.id.temp_min);
        iTemp_max = findViewById(R.id.temp_max);
        iSunrise = findViewById(R.id.sunrise);
        iSunset = findViewById(R.id.sunset);
        iWind = findViewById(R.id.wind);
        iPressure = findViewById(R.id.pressure);
        iHumidity = findViewById(R.id.humidity);
        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loader.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                showSettingsAlertPer();
            }
            new weatherTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation() {
        gpsTracker = new WeatherGpsTracker(WeatherDetails.this);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lon = gpsTracker.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(lon);
        } else {
            showSettingsAlert();
        }
    }


    //Alert dialog for turning on GPS
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeatherDetails.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("Please enable GPS Service for better performance");

        // On pressing Settings button
        alertDialog.setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                WeatherDetails.this.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }


    //Alert dialog for changing location permissions
    public void showSettingsAlertPer() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("App Permissions");

        // Setting Dialog Message
        alertDialog.setMessage("Please allow location permission before continuing");

        // On pressing Settings button
        alertDialog.setPositiveButton("Allow GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }



    public final class weatherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(WeatherDetails.this,"Json Data is downloading",Toast.LENGTH_SHORT).show();
            loader.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
        }

        protected Void doInBackground(Void... arg0) {
            WeatherHttpHandler sh = new WeatherHttpHandler();
            // Making a request to url and getting response
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + API;
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                    long updatedAt = jsonObj.getLong("dt");
                    String updatedAtText = "Updated at: " + (new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)).format(new Date(updatedAt * (long) 1000));
                    String temp = main.getString("temp") + "°C";
                    String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                    String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");
                    long sunrise = sys.getLong("sunrise");
                    long sunset = sys.getLong("sunset");
                    String windSpeed = wind.getString("speed");
                    String weatherDescription = weather.getString("description");
                    String address = jsonObj.getString("name") + ", " + sys.getString("country");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iAddress.setText(address);
                            iUpdated_at.setText(updatedAtText);
                            iStatus.setText(weatherDescription.toUpperCase(Locale.ROOT));
                            iTemp.setText(temp);
                            iTemp_min.setText(tempMin);
                            iTemp_max.setText(tempMax);
                            iSunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                            iSunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                            String tempWind = windSpeed + " km/h";
                            iWind.setText(tempWind);
                            String tempPressure = pressure + " mbar";
                            iPressure.setText(tempPressure);
                            String tempHumidity = humidity + "%";
                            iHumidity.setText(tempHumidity);

                            loader.setVisibility(View.GONE);
                            mainContainer.setVisibility(View.VISIBLE);
                        }
                    });

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    */

                }

            } else {
                Log.e(TAG, "Couldn't get json from server");
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                */
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}

