package com.example.rahulmalhotra.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.widget.TextView;

import com.example.rahulmalhotra.bakingapp.Adapters.RecipeStepAdapter;
import com.example.rahulmalhotra.bakingapp.Objects.Ingredient;
import com.example.rahulmalhotra.bakingapp.Objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetail extends AppCompatActivity {

    @BindView(R.id.ingredientList)
    TextView ingredientsListView;

    @BindView(R.id.recipeStepsList)
    RecyclerView recipeStepsView;

    List<Ingredient> ingredientList;
    List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ingredientList = getIntent().getParcelableArrayListExtra("recipeIngredients");
        stepList = getIntent().getParcelableArrayListExtra("recipeSteps");
        String recipeName = getIntent().getStringExtra("recipeName");

        getSupportActionBar().setTitle(recipeName);

        SpannableStringBuilder ssb = getSpannableStringBuilder(ingredientList);

        ingredientsListView.setText(ssb);

        RecipeStepAdapter adapter = new RecipeStepAdapter(this);
        adapter.setRecipeStepList(stepList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipeStepsView.setLayoutManager(layoutManager);
        recipeStepsView.setHasFixedSize(true);
        recipeStepsView.setAdapter(adapter);

    }

    public static SpannableStringBuilder getSpannableStringBuilder(List<Ingredient> ingredientList) {

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

        return  ssb;
    }
}
