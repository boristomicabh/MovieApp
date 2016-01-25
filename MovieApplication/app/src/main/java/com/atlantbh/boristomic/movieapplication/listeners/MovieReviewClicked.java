package com.atlantbh.boristomic.movieapplication.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.atlantbh.boristomic.movieapplication.utils.Constants;

/**
 * Created by boristomic on 24/01/16.
 */
public class MovieReviewClicked implements View.OnClickListener {

    private long movieId;
    private Context context;
    private int movieType;

    public MovieReviewClicked(long movieId, Context context, int movieType) {
        this.movieId = movieId;
        this.context = context;
        this.movieType = movieType;
    }

    /**
     * New activity is started, browser opens movie review page or tv show info page
     *
     * @param v <code>View</code> with info icon
     */
    @Override
    public void onClick(View v) {
        if (Constants.MOVIE == movieType) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MOVIE_REVIEW_URL_BASE + movieId + Constants.MOVIE_REVIEW_URL_EXTRA)));
        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TV_SHOW_INFO_URL_BASE + movieId)));
        }
    }
}
