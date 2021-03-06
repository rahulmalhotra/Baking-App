package com.example.rahulmalhotra.bakingapp;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.rahulmalhotra.bakingapp.API.BakingAPIService;
import com.example.rahulmalhotra.bakingapp.API.RetrofitClient;
import com.example.rahulmalhotra.bakingapp.Adapters.RecipeAdapter;
import com.example.rahulmalhotra.bakingapp.IdlingResource.SimpleIdlingResource;
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

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(simpleIdlingResource==null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }

    private void setSimpleIdlingResourceIdleState(boolean idleState) {
        if(simpleIdlingResource!=null) {
            simpleIdlingResource.setIdleState(idleState);
        }
    }

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

        getIdlingResource();
        setSimpleIdlingResourceIdleState(false);

        // Making Api Callout
        final RecipeAdapter adapter = new RecipeAdapter(this);
        if(isNetworkAvailable()) {
            BakingAPIService bakingAPIService = RetrofitClient.getClient("https://d17h27t6h515a5.cloudfront.net/").create(BakingAPIService.class);
            bakingAPIService.getRecipes().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if(response.isSuccessful()) {
                        adapter.setRecipeList(response.body());
                        setSimpleIdlingResourceIdleState(true);
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    setSimpleIdlingResourceIdleState(true);
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
        if(connectivityManager==null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }
}