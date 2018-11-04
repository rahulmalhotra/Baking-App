package com.example.rahulmalhotra.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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
import com.example.rahulmalhotra.bakingapp.Objects.Step;
import com.example.rahulmalhotra.bakingapp.R;
import com.example.rahulmalhotra.bakingapp.RecipeDetail;
import com.example.rahulmalhotra.bakingapp.RecipeStepDetail;
import com.example.rahulmalhotra.bakingapp.RecipeStepDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private ArrayList<Step> recipeStepList;
    private Context activityContext;
    private boolean isTablet;

    public RecipeStepAdapter(Context context) {
        this.activityContext = context;
    }

    public void setRecipeStepList(List<Step> recipeStepList) {
        this.recipeStepList = new ArrayList<>(recipeStepList);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activityContext).inflate(R.layout.recipe_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = recipeStepList.get(holder.getAdapterPosition());
        holder.recipeStepDescription.setText(step.getShortDescription());
        if(!step.getThumbnailURL().isEmpty()) {
            Picasso.get().load(step.getThumbnailURL()).into(holder.recipeStepImage);
        } else {
            Picasso.get().load(R.drawable.recipeicon).into(holder.recipeStepImage);
        }
    }

    @Override
    public int getItemCount() {
        if(recipeStepList!=null)
            return recipeStepList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeStepImage)
        public ImageView recipeStepImage;
        @BindView(R.id.recipeStep)
        public TextView recipeStepDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

            Step step = recipeStepList.get(getAdapterPosition());
            Bundle arguments = new Bundle();
            isTablet = activityContext.getResources().getBoolean(R.bool.isTablet);

            if(isTablet) {
                arguments.putString("recipeDescription",step.getDescription());
                arguments.putString("recipeVideoURL", step.getVideoURL());
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                if(arguments!=null) {
                    fragment.setArguments(arguments);
                }
                ((AppCompatActivity)activityContext).getSupportFragmentManager().beginTransaction().replace(R.id.recipeStepDetailContainer, fragment).commit();
            } else {
                arguments.putParcelableArrayList("stepList", new ArrayList<>(recipeStepList));
                arguments.putInt("activeStepNumber", getAdapterPosition());
                Intent intent = new Intent(activityContext, RecipeStepDetail.class);
                intent.putExtras(arguments);
                activityContext.startActivity(intent);
            }
        }
    }
}