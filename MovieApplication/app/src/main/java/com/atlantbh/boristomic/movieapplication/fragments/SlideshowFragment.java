package com.atlantbh.boristomic.movieapplication.fragments;

import android.os.Bundle;
import android.util.Log;

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
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                player.play();
                if (!b) {
                    player.loadVideo(getArguments().getString(Constants.FRAGMENT_KEY));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(LOG_TAG, "Failed to initialize video");
            }
        });
    }

    @Override
    public void onPause() {
        player.pause();
        super.onPause();
    }
}
