package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private final String LOG_TAG = TrailerActivity.class.getSimpleName();

    private YouTubePlayer player;
    private String videoKey;
    private YouTubePlayerView trailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        trailer = (YouTubePlayerView) findViewById(R.id.youtube_trailer);
        trailer.initialize(Constants.YOU_TUBE_API_KEY, this);

        final Intent intent = getIntent();
        videoKey = intent.getStringExtra(Constants.INTENT_KEY);
    }

    /**
     * Starts player in fullscreen if player load successfully
     *
     * @param provider      <code>YouTubePlayer.Provider</code> type object
     * @param youTubePlayer <code>YouTubePlayer</code> type object
     * @param b             <code>boolean</code> type value if state was restored
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        player = youTubePlayer;
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        player.setFullscreen(true);
        if (!b) {
            player.loadVideo(videoKey);
        }
    }

    /**
     * Logs error message if player fail to initialize
     *
     * @param provider                    <code>YouTubePlayer.Provider</code> type object
     * @param youTubeInitializationResult <code>YouTubePlayer</code> type object
     */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e(LOG_TAG, "Error initializing YouTubePlayer");
    }

}
