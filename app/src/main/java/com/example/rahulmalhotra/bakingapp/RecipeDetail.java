package com.example.rahulmalhotra.bakingapp;

import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rahulmalhotra.bakingapp.Objects.Ingredient;
import com.example.rahulmalhotra.bakingapp.Objects.Recipe;
import com.example.rahulmalhotra.bakingapp.Objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetail extends AppCompatActivity {

    @BindView(R.id.ingredientList)
    TextView ingredientsListView;

    @BindView(R.id.recipeStepsList)
    ListView recipeStepsView;

    List<Ingredient> ingredientList;
    List<Step> stepList;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        ingredientList = getIntent().getParcelableArrayListExtra("recipeIngredients");
        stepList = getIntent().getParcelableArrayListExtra("recipeSteps");
        String recipeName = getIntent().getStringExtra("recipeName");

        getSupportActionBar().setTitle(recipeName);

        StringBuilder sb = new StringBuilder();
        for(Integer i=0; i<ingredientList.size(); i++) {
            Ingredient ingredient = ingredientList.get(i);
            sb.append(ingredient.getIngredient());
            sb.append(" (");
            sb.append(ingredient.getQuantity());
            sb.append(" ");
            sb.append(ingredient.getMeasure());
            sb.append(")");
            sb.append("\n");
        }

        String ingredientListArray[] = sb.toString().split("\n");

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for(int i=0; i<ingredientListArray.length; i++) {
            String line = ingredientListArray[i];
            SpannableString ss = new SpannableString(line);
            ss.setSpan(new BulletSpan(15), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
            if(i+1<ingredientListArray.length) {
                ssb.append("\n");
            }
        }

        ingredientsListView.setText(ssb);

        String[] descriptionArray = new String[stepList.size()];
        for(int i=0; i<stepList.size(); i++) {
            Step recipeStep = stepList.get(i);
            descriptionArray[i] = /*String.valueOf(i+1) + ". " +*/ recipeStep.getShortDescription();
        }

        ArrayAdapter<String> recipeStepsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, android.R.id.text1, descriptionArray);
        recipeStepsView.setAdapter(recipeStepsAdapter);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        recipeStepsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Step step = stepList.get(i);

                Bundle arguments = new Bundle();
                arguments.putString("recipeDescription",step.getDescription());
                arguments.putString("recipeVideoURL", step.getVideoURL());

                if(isTablet) {
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    if(arguments!=null) {
                        fragment.setArguments(arguments);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.recipeStepDetailContainer, fragment, RecipeDetail.class.getName()).commit();
                } else {
                    Intent intent = new Intent(RecipeDetail.this, RecipeStepDetail.class);
                    intent.putExtras(arguments);
                    startActivity(intent);
                }
            }
        });
    }
}
