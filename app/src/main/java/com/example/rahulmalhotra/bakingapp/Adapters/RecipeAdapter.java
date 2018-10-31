package com.example.rahulmalhotra.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahulmalhotra.bakingapp.MainActivity;
import com.example.rahulmalhotra.bakingapp.Objects.Recipe;
import com.example.rahulmalhotra.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList;

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(holder.getLayoutPosition());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipeImage)
        public ImageView recipeImage;
        @BindView(R.id.recipeTitle)
        public TextView recipeTitle;
        @BindView(R.id.recipeServing)
        public TextView recipeServing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}