package com.atlantbh.boristomic.movieapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.activities.TrailerActivity;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by boristomic on 05/02/16.
 */
public class SlideshowFragment extends YouTubePlayerSupportFragment {

    private final String LOG_TAG = SlideshowFragment.class.getSimpleName();

    private YouTubePlayer player;
    private int count = 0;

    public static SlideshowFragment newInstance(String videoID) {
        SlideshowFragment fragment = new SlideshowFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_KEY, videoID);
        fragment.setArguments(args);
        fragment.init();
        return fragment;
    }

    private void init() {
        initialize(Constants.YOU_TUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(final YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                final String videoId = getArguments().getString(Constants.FRAGMENT_KEY);
                player = youTubePlayer;
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                if (!b) {
                    player.loadVideo(videoId);
                }
                player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        count++;
                        if (count > 1) {
                            player.release();
                            Intent intent = new Intent(MovieActivity.getContext(), TrailerActivity.class);
                            intent.putExtra(Constants.INTENT_KEY, videoId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPaused() {
                        Log.v(LOG_TAG, "onPaused() called");
                    }

                    @Override
                    public void onStopped() {
                        Log.v(LOG_TAG, "onStopped() called");
                    }

                    @Override
                    public void onBuffering(boolean b) {
                        Log.v(LOG_TAG, "onBuffering() called");
                    }

                    @Override
                    public void onSeekTo(int i) {
                        Log.v(LOG_TAG, "onSeekTo() called");
                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(LOG_TAG, "Failed to initialize video");
            }
        });
    }

}
