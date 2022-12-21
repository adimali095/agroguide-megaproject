package com.example.market_price;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {


    public RecyclerView recyclerView;
    int currentItems, totalItems, scrolledItems;
    private int offset = 0;
    private int limit = 20;
    private GoAdapter goAdapter;
    private List<GoiDetail> goiDetails;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private Boolean isScrolling = false;
    private EditText searchText;
    private ImageButton searchButton1;
    private String districtString = null;
    private String apiReq = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;

    void updateApiReq(String district, String state) {
        String temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
        if (!state.equals("")) {            //State is not empty
            if (!district.equals("")) {     //State & District is not empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&filters[state]=" + state + "&filters[district]=" + district + "&offset=" + offset;
            } else {                        //District is empty but state not empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
            }
        } else {                            //State is empty
            if (!district.equals("")) {     //State is empty but district not empty...This is called regularly
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&filters[district]=" + district + "&offset=" + offset;
            } else {                        //State and district both empty
                temp = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5&format=json&limit=" + limit + "&offset=" + offset;
            }
        }
        apiReq = temp;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        FilterActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        toolbar = findViewById(R.id.BarLayout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Market Prices");
        progressBar = findViewById(R.id.progressBar2);
        progressBar2 = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.filterRecyclerView);
        goiDetails = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        goAdapter = new GoAdapter(FilterActivity.this, goiDetails);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(goAdapter);

        searchText = findViewById(R.id.searchByDistrict);
        searchButton1 = findViewById(R.id.searchButton1);

        searchText.requestFocus();
        try {
            searchButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar2.setVisibility(View.VISIBLE);
                    districtString = String.valueOf(searchText.getText());
                    offset = 0;
                    limit = 20;
                    updateApiReq(districtString, "");
                    new DownloadTask().execute(apiReq);
                }
            });
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    updateApiReq(districtString, "");
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
        getMenuInflater().inflate(R.menu.filtermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.sortByPrice) {
            if (goiDetails.isEmpty()) {
                Toast.makeText(this, "No data for sorting", Toast.LENGTH_SHORT).show();
            } else {
                Collections.sort(goiDetails, new Comparator<GoiDetail>() {
                    @Override
                    public int compare(GoiDetail g1, GoiDetail o2) {
                        return Integer.valueOf(g1.getGroceryPrice()).compareTo(Integer.valueOf(o2.getGroceryPrice()));
                    }
                });
                goAdapter.notifyDataSetChanged();
            }

        } else if (item.getItemId() == R.id.sortByName) {
            if (goiDetails.isEmpty()) {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            } else {
                Collections.sort(goiDetails, new Comparator<GoiDetail>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public int compare(GoiDetail g1, GoiDetail g2) {
                        //return Integer.compare(Math.toIntExact(g1.getGroceryTime()), Math.toIntExact(g2.getGroceryTime()));
                        String name1 = g1.getGroceryName();
                        String name2 = g2.getGroceryName();
                        return name1.compareToIgnoreCase(name2);
                    }
                });
                goAdapter.notifyDataSetChanged();
            }

        } else if (item.getItemId() == android.R.id.home) {
            finish();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
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
                    goAdapter = new GoAdapter(FilterActivity.this, goiDetails);
                    synchronized (goiDetails) {
                        goiDetails.notify();//notifyDatasetChanged
                    }
                    recyclerView.setAdapter(goAdapter);

                    if (totalItems == (currentItems + scrolledItems))
                        recyclerView.scrollToPosition(totalItems - currentItems + 1);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(FilterActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

            }
            progressBar2.setVisibility(View.GONE);
            if (goiDetails.isEmpty()) {
                Toast.makeText(FilterActivity.this, "Data not fetched... Enter first Latter Capital Always", Toast.LENGTH_SHORT).show();
            }


        }
    }

}

