package com.example.rahulmalhotra.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.recipePlayer)
    PlayerView recipePlayerView;

    @BindView(R.id.recipeStepDetail)
    TextView recipeStepDetailTextView;

    Context activityContext;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        this.activityContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.video_and_step_detail, container, false);
        ButterKnife.bind(this, fragmentView);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(activityContext);
        recipePlayerView.setPlayer(player);
        recipeStepDetailTextView.setText("Dummy Text For Fragment");

        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            recipePlayerView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recipePlayerView.getLayoutParams();
            params.height = params.WRAP_CONTENT;
            recipePlayerView.setLayoutParams(params);
        }

        return fragmentView;
    }
}
