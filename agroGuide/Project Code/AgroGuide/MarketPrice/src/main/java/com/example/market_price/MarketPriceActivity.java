package com.example.market_price;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarketPriceActivity extends AppCompatActivity {

    int currentItems, totalItems, scrolledItems;
    private int offset = 0;
    private final int limit = 20;
    private GoAdapter goAdapter;
    private RecyclerView recyclerView;
    private List<GoiDetail> goiDetails;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private Boolean isScrolling = false;

    private String apiReq = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;

    void updateApiReq(String district, String state) {
        String temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
        if (!state.equals("")) {                //State is not empty
            if (!district.equals("")) {         //State & District is not empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&filters[state]=" + state + "&filters[district]=" + district + "&offset=" + offset;
            } else {                            //District is empty but state not empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
            }
        } else {                                 //State is empty
            if (!district.equals("")) {          //State is empty but district not empty...This is called regularly
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&filters[district]=" + district + "&offset=" + offset;
            } else {                             //State and district both empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
            }
        }
        apiReq = temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketprice);

        MarketPriceActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        recyclerView = findViewById(R.id.recyclerview);
        goiDetails = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        goAdapter = new GoAdapter(MarketPriceActivity.this, goiDetails);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(goAdapter);


        toolbar = findViewById(R.id.BarLayout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Market Prices");

        try {
            //updateApiReq("Kolhapur", "Maharashtra");
            updateApiReq("", "");
            new DownloadTask().execute(apiReq);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadData();
    }

    private void loadData() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledItems) == totalItems) {
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    fetchData();
                }
            }
        });
    }

    private void fetchData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    offset += 20;
                    //limit += 20;
                    updateApiReq("", "");
                    new DownloadTask().execute(apiReq);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.searchByDistrict) {
            Intent filterIntent = new Intent(MarketPriceActivity.this, FilterActivity.class);
            startActivity(filterIntent);
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected synchronized void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String records = "";
                    records = jsonObject.getString("records");
                    JSONArray arr = new JSONArray(records);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject part = arr.getJSONObject(i);
                        GoiDetail details = new GoiDetail();
                        details.setGroceryName(part.getString("commodity"));
                        details.setGroceryPlace(part.getString("district") + "," + part.getString("state"));
                        details.setGroceryPrice(part.getString("modal_price"));
                        details.setGroceryTime(part.getString("arrival_date"));

                        Log.i("workAPI", part.getString("district"));
                        goiDetails.add(details);
                    }
                    goAdapter = new GoAdapter(MarketPriceActivity.this, goiDetails);
                    synchronized (goiDetails) {
                        goiDetails.notify();//notifyDatasetChanged
                    }
                    recyclerView.setAdapter(goAdapter);
                    recyclerView.scrollToPosition(totalItems - currentItems);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new DownloadTask().execute(apiReq);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}