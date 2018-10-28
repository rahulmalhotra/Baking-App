package com.example.rahulmalhotra.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rahulmalhotra.bakingapp.API.BakingAPIService;
import com.example.rahulmalhotra.bakingapp.API.RetrofitClient;
import com.example.rahulmalhotra.bakingapp.Objects.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recipeList)
    public RecyclerView recipeListRecyclerView;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("test", String.valueOf(recipeListRecyclerView));
        BakingAPIService bakingAPIService = RetrofitClient.getClient("https://d17h27t6h515a5.cloudfront.net/").create(BakingAPIService.class);
        bakingAPIService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    Log.d( TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d( TAG, t.getMessage());
            }
        });
    }
}
