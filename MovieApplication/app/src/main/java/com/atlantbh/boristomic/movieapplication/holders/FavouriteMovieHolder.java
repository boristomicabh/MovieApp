package com.atlantbh.boristomic.movieapplication.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;
import com.atlantbh.boristomic.movieapplication.utils.Connection;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boristomic on 30/01/16.
 */
public class FavouriteMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.list_movie_poster)
    protected ImageView mMoviePoster;
    @Bind(R.id.movie_rating_bar)
    protected RatingBar mMovieRatingStars;
    @Bind(R.id.movie_rating_number)
    protected TextView mMovieRatingNumber;
    @Bind(R.id.movie_title)
    protected TextView mMovieTitle;
    @Bind(R.id.movie_overview)
    protected TextView mMovieOverview;

    private Context context;
    private long movieId;
    private int type;

    public FavouriteMovieHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(Constants.INTENT_KEY, movieId);
        if (type == Constants.TV_SHOWS) {
            intent.putExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, Constants.TV_SHOWS);
        }
        context.startActivity(intent);
    }

    public void bindMovie(MovieDB temp) {
        this.movieId = temp.getId();
        type = Constants.MOVIE;
        if (temp.getName() == null) {
            mMovieTitle.setText(temp.getTitle());
        } else {
            type = Constants.TV_SHOWS;
            mMovieTitle.setText(temp.getName());
        }
        mMovieRatingStars.setRating(temp.getVote());
        mMovieRatingNumber.setText(temp.getVoteAverage());
        if (temp.getOverview().length() > 250) {
            mMovieOverview.setText(temp.getOverview().substring(0, 230) + "...");
        } else {
            mMovieOverview.setText(temp.getOverview());
        }
        if (!Connection.isConnected(context) || temp.getPosterPath() == null || temp.getPosterPath().length() == 0) {
            Picasso.with(context).load(R.drawable.poster_default).into(mMoviePoster);
        } else {
            Picasso.with(context).load(Constants.URL_BASE_IMG + Constants.POSTER_SIZE_W185 + "/" + temp.getPosterPath()).into(mMoviePoster);
        }
    }
}
