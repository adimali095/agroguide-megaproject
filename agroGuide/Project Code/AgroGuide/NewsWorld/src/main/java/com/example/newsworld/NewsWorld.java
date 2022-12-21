package com.example.newsworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsWorld extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{


    //579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5
    //44c570125e4b41c4a064a06b6e0a355e
    //https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?format=json&offset=0&limit=2000&filters[state]=Maharashtra&filters[district]=Sangli&api-key=579b464db66ec23bdd000001846988015f784fb755f03d1d929ed4d5

    private RecyclerView newsRV, categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, this, this::onCategoryCLick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories() {
        categoryRVModalArrayList.add(new CategoryRVModal("All", "https://static9.depositphotos.com/1011646/1236/i/600/depositphotos_12369509-stock-photo-breaking-news-screen.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology", "https://assets.thehansindia.com/h-upload/2021/07/31/1092805-tech.webp"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science", "https://img.freepik.com/free-vector/science-word-orange-background-concept_23-2148548239.jpg?size=626&ext=jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports", "https://img.freepik.com/free-vector/sports-news-banner-blue-background-illustration_275806-114.jpg?size=626&ext=jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("General", "https://thumbs.dreamstime.com/b/old-vintage-newspaper-realistic-pages-templates-you-title-header-edition-name-text-isolated-vector-illustration-old-vintage-158781916.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Business", "https://ak.picdn.net/shutterstock/videos/4511768/thumb/8.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment", "https://ak.picdn.net/shutterstock/videos/4511768/thumb/8.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Health", "https://ak.picdn.net/shutterstock/videos/4511768/thumb/8.jpg"));

        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category) {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apiKey=44c570125e4b41c4a064a06b6e0a355e";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=44c570125e4b41c4a064a06b6e0a355e";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if (category.equals("All")) {
            call = retrofitAPI.getAllNews(url);
        } else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }

                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(NewsWorld.this, "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryCLick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}