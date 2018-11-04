package com.example.rahulmalhotra.bakingapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.rahulmalhotra.bakingapp.Objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetail extends AppCompatActivity {

    @BindView(R.id.prevStepBtn)
    FloatingActionButton prevStepBtn;

    @BindView(R.id.nextStepBtn)
    FloatingActionButton nextStepBtn;

    List<Step> stepList;
    Integer activeStepNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getExtras();
        if(arguments.containsKey("stepList")) {
            stepList = arguments.getParcelableArrayList("stepList");
        }
        if(arguments.containsKey("activeStepNumber")) {
            activeStepNumber = arguments.getInt("activeStepNumber");
        }

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            prevStepBtn.setVisibility(View.GONE);
            nextStepBtn.setVisibility(View.GONE);
        }

        if(stepList!=null) {
            setFragment();
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                addButtonClickListeners();
            }
        }
    }

    private void addButtonClickListeners() {
        prevStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeStepNumber--;
                if(activeStepNumber<0) {
                    activeStepNumber = stepList.size()-1;
                }
                setFragment();
            }
        });

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeStepNumber++;
                if(activeStepNumber==stepList.size()) {
                    activeStepNumber = 0;
                }
                setFragment();
            }
        });
    }

    private void setFragment() {
        Bundle arguments = new Bundle();
        Step step = stepList.get(activeStepNumber);
        getSupportActionBar().setTitle(step.getShortDescription());
/*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        arguments.putString("recipeDescription",step.getDescription());
        arguments.putString("recipeVideoURL", step.getVideoURL());
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipeStepDetailContainer, fragment).commit();
    }
}
