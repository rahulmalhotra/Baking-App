package com.example.rahulmalhotra.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.sql.DataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.recipePlayer)
    PlayerView recipePlayerView;

    @BindView(R.id.recipeStepDetail)
    TextView recipeStepDetailTextView;

    String recipeDescription, recipeVideoURL;

    Context activityContext;
    boolean isTablet;
    SimpleExoPlayer recipePlayer;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.activityContext = getActivity();
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if(getArguments().containsKey("recipeDescription")) {
            recipeDescription = getArguments().getString("recipeDescription");
        }
        if(getArguments().containsKey("recipeVideoURL")) {
            recipeVideoURL = getArguments().getString("recipeVideoURL");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(recipePlayer!=null) {
            recipePlayer.stop();
            recipePlayer.release();
            recipePlayer = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.video_and_step_detail, container, false);
        ButterKnife.bind(this, fragmentView);
        recipePlayer = ExoPlayerFactory.newSimpleInstance(activityContext);
        recipePlayerView.setPlayer(recipePlayer);

        if(!recipeDescription.isEmpty()) {
            recipeStepDetailTextView.setText(recipeDescription);
        } else {
            recipeStepDetailTextView.setText("No Step Description Found");
        }

        if(!recipeVideoURL.isEmpty()) {
            String userAgent = Util.getUserAgent(activityContext, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(recipeVideoURL), new DefaultDataSourceFactory(
                    activityContext, userAgent), new DefaultExtractorsFactory(), null, null);
            recipePlayer.prepare(mediaSource);
            recipePlayer.setPlayWhenReady(true);
        }

        if (!isTablet && getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            recipePlayerView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recipePlayerView.getLayoutParams();
            params.height = params.MATCH_PARENT;
            recipePlayerView.setLayoutParams(params);
        }

        return fragmentView;
    }
}
