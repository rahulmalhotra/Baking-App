package com.example.rahulmalhotra.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeStepDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Bundle arguments = getIntent().getExtras();

        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        if(arguments!=null) {
            fragment.setArguments(arguments);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.recipeStepDetailContainer, fragment).commit();
    }
}
