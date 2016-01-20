package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView movieBackdrop = (ImageView) this.findViewById(R.id.backdrop_movie_image);
        Intent intent = getIntent();
        long movieId = intent.getLongExtra(Constants.INTENT_KEY, 1);

        MovieAPI api = RestService.get();

        api.findSingleMovie(movieId, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {

                String backdropPath = MovieUtils.getBackdropURL(Constants.BACKDROP_SIZE_W1280, movie);

                if (backdropPath == null) {
                    Picasso.with(MovieActivity.this).load(R.drawable.default_backdrop).into(movieBackdrop);
                } else {
                    Picasso.with(MovieActivity.this).load(backdropPath).into(movieBackdrop);
                }
        }

        @Override
        public void failure (RetrofitError error){
            Log.e(LOG_TAG, "Failed to load single movie", error);

        }
    }

    );
}

}
