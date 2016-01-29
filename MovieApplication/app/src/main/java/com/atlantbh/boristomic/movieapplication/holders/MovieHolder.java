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
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boristomic on 28/01/16.
 */
public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    private long movieId;
    private int listType;
    private Context context;

    /**
     * Public constructor of MovieHolder class, used to get context so it can be used initiate other activities.
     * Binds all view properties using ButterKnife, sets on click listener to entire view and
     * initializes listType parameter used to get appropriate date.
     *
     * @param itemView <code>View</code> type object
     * @param context  <code>Context</code> type object of context from it's being used
     * @param type     <code>int</code> type of list being created and used
     */
    public MovieHolder(View itemView, Context context, int type) {
        super(itemView);
        this.context = context;
        this.listType = type;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Method used to bind one element to RecyclerView list
     *
     * @param movie <code>Movie</code> type object
     */
    public void bindMovie(Movie movie) {
        this.movieId = movie.getId();

        final Movie temp = movie;
        mMovieTitle.setText(MovieUtils.getTitleWithYear(temp, listType));
        Picasso.with(context).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, temp)).into(mMoviePoster);
        mMovieRatingStars.setRating(MovieUtils.getMovieFloatRating(movie));
        mMovieRatingNumber.setText(MovieUtils.getMovieStringRating(movie));
        mMovieOverview.setText(MovieUtils.getShorterOverview(movie));
    }

    /**
     * Movie activity is started and movie id is passed as parameter or
     * tv show is passed as parameter if tv show is selected
     *
     * @param v <code>View</code> with movie or tv show image and short info
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.INTENT_KEY, movieId);
        if (listType == Constants.TV_SHOWS) {
            intent.putExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, Constants.TV_SHOWS);
        }
        context.startActivity(intent);
    }

}
