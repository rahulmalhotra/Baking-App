package com.example.rahulmalhotra.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
    Long playerPosition;
    int playerState;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        playerPosition = 0L;
        if(savedInstanceState!=null) {
            playerPosition = savedInstanceState.getLong("playerPosition");
            playerState = savedInstanceState.getInt("playerState");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        playerPosition = recipePlayer.getCurrentPosition();
        playerState = recipePlayer.getPlaybackState();
        outState.putLong("playerPosition", playerPosition);
        outState.putInt("playerState", playerState);
        super.onSaveInstanceState(outState);
    }

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
        playerPosition = 0L;
        if(savedInstanceState!=null) {
            playerPosition = savedInstanceState.getLong("playerPosition");
            playerState = savedInstanceState.getInt("playerState");
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
            recipeStepDetailTextView.setText(R.string.noDescription);
        }

        if(!recipeVideoURL.isEmpty()) {
            String userAgent = Util.getUserAgent(activityContext, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(
                    activityContext, userAgent)).createMediaSource(Uri.parse(recipeVideoURL));
            recipePlayer.prepare(mediaSource);
            if(playerState == PlaybackState.STATE_PAUSED)
                recipePlayer.setPlayWhenReady(false);
            else
                recipePlayer.setPlayWhenReady(true);
            recipePlayer.seekTo(playerPosition);
        }

        if (!isTablet && getActivity()!=null && getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            recipePlayerView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            if(getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recipePlayerView.getLayoutParams();
            params.height = params.MATCH_PARENT;
            recipePlayerView.setLayoutParams(params);
        }

        return fragmentView;
    }
}
