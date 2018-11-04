package com.example.rahulmalhotra.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahulmalhotra.bakingapp.MainActivity;
import com.example.rahulmalhotra.bakingapp.Objects.Ingredient;
import com.example.rahulmalhotra.bakingapp.Objects.Recipe;
import com.example.rahulmalhotra.bakingapp.R;
import com.example.rahulmalhotra.bakingapp.RecipeDetail;
import com.example.rahulmalhotra.bakingapp.RecipeIngredientsWidgetService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<Recipe> recipeList;
    private Context activityContext;

    public RecipeAdapter(Context context) {
        this.activityContext = context;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = new ArrayList<>(recipeList);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activityContext).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(holder.getAdapterPosition());
        holder.recipeTitle.setText(recipe.getName());
        holder.recipeServing.setText("Servings: " + String.valueOf(recipe.getServings()));
        if(!recipe.getImage().isEmpty()) {
            Picasso.get().load(recipe.getImage()).into(holder.recipeImage);
        } else {
            Picasso.get().load(R.drawable.recipeicon).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        if(recipeList!=null)
            return recipeList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeImage)
        public ImageView recipeImage;
        @BindView(R.id.recipeTitle)
        public TextView recipeTitle;
        @BindView(R.id.recipeServing)
        public TextView recipeServing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = recipeList.get(getAdapterPosition());
            Intent intent = new Intent(activityContext, RecipeDetail.class);
            RecipeIngredientsWidgetService.startActionUpdateRecipeIngredients(activityContext, recipe);
            intent.putExtra("recipeName", recipe.getName());
            intent.putParcelableArrayListExtra("recipeIngredients", new ArrayList<Parcelable>(recipe.getIngredients()));
            intent.putParcelableArrayListExtra("recipeSteps", new ArrayList<Parcelable>(recipe.getSteps()));
            activityContext.startActivity(intent);
        }
    }
}