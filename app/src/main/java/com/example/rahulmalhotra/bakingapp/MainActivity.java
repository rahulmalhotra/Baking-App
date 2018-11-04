package com.example.rahulmalhotra.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rahulmalhotra.bakingapp.API.BakingAPIService;
import com.example.rahulmalhotra.bakingapp.API.RetrofitClient;
import com.example.rahulmalhotra.bakingapp.Adapters.RecipeAdapter;
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
    private boolean isTablet;
    private Integer recyclerViewColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Setting layout according to screen orientation and size
        isTablet = getResources().getBoolean(R.bool.isTablet);
        Integer screenOrientation = this.getResources().getConfiguration().orientation;
        if(isTablet) {
            if(screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerViewColumns = 2;
            } else {
                recyclerViewColumns = 3;
            }
        } else {
            if(screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerViewColumns = 1;
            } else {
                recyclerViewColumns = 2;
            }
        }

        // Making Api Callout
        final RecipeAdapter adapter = new RecipeAdapter(this);
        if(isNetworkAvailable()) {
            BakingAPIService bakingAPIService = RetrofitClient.getClient("https://d17h27t6h515a5.cloudfront.net/").create(BakingAPIService.class);
            bakingAPIService.getRecipes().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if(response.isSuccessful()) {
                        adapter.setRecipeList(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Log.d( TAG, t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Device is not connected to internet", Toast.LENGTH_SHORT).show();
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, recyclerViewColumns);
        recipeListRecyclerView.setLayoutManager(layoutManager);
        recipeListRecyclerView.setHasFixedSize(true);
        recipeListRecyclerView.setAdapter(adapter);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }
}