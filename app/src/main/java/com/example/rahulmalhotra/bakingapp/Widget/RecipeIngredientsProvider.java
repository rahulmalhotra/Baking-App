package com.example.rahulmalhotra.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.RemoteViews;

import com.example.rahulmalhotra.bakingapp.Objects.Recipe;
import com.example.rahulmalhotra.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsProvider extends AppWidgetProvider {

    private static String activeIngredientsList;
    private static String activeRecipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, String recipeName, String ingredientsList) {
        activeRecipeName = recipeName;
        activeIngredientsList = ingredientsList;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_provider);
        views.setTextViewText(R.id.recipeName, activeRecipeName);
        views.setTextViewText(R.id.recipeIngredients, activeIngredientsList);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId}, activeRecipeName, activeIngredientsList);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

