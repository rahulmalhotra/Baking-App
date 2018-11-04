package com.example.rahulmalhotra.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

import com.example.rahulmalhotra.bakingapp.Objects.Recipe;

public class RecipeIngredientsWidgetService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_INGREDIENTS = "com.example.rahulmalhotra.bakingapp.action.update_ingredients";
    private static Recipe activeRecipe;

    public RecipeIngredientsWidgetService() {
        super("RecipeIngredientsWidgetService");
    }

    public RecipeIngredientsWidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null) {
            String action = intent.getAction();
            if(ACTION_UPDATE_RECIPE_INGREDIENTS.equals(action)) {
                handleActionUpdateRecipeIngredients();
            }
        }
    }

    private void handleActionUpdateRecipeIngredients() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientsProvider.class));
        SpannableStringBuilder sbb = RecipeDetail.getSpannableStringBuilder(activeRecipe.getIngredients());
        RecipeIngredientsProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, activeRecipe.getName(), sbb.toString());
    }

    public static void startActionUpdateRecipeIngredients(Context context, Recipe recipe) {
        activeRecipe = recipe;
        Intent intent = new Intent(context, RecipeIngredientsWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_INGREDIENTS);
        context.startService(intent);
    }
}