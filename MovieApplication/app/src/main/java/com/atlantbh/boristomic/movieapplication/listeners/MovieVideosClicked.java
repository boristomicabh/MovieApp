package com.atlantbh.boristomic.movieapplication.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.utils.Constants;

/**
 * Created by boristomic on 24/01/16.
 */
public class MovieVideosClicked implements View.OnClickListener {

    private String trailerKey;
    private Context context;

    public MovieVideosClicked(String trailerKey, Context context) {
        this.trailerKey = trailerKey;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (trailerKey == null) {
            Toast.makeText(context, "No YouTube trailer for this movie", Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + trailerKey)));
        }

    }
}
