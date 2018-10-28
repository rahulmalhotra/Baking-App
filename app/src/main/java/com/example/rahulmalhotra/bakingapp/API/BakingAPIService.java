package com.example.rahulmalhotra.bakingapp.API;

import com.example.rahulmalhotra.bakingapp.Objects.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingAPIService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
