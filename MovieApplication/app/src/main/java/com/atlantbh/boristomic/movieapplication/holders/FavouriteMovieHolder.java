package com.atlantbh.boristomic.movieapplication.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boristomic on 30/01/16.
 */
public class FavouriteMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.favourite_movie_title)
    protected TextView mMovieTitle;
    @Bind(R.id.favourite_movie_remove_button)
    protected ImageView mRemoveMovie;

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

    public ImageView getRemoveMovie() {
        return mRemoveMovie;
    }

    public TextView getMovieTitle() {
        return mMovieTitle;
    }

    public void setIdAndYpe(long movieId, int type) {
        this.movieId = movieId;
        this.type = type;
    }

}
