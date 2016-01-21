package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.CreditsAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.ImageAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.MovieAdapter;
import com.atlantbh.boristomic.movieapplication.models.Cast;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.models.Videos;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.parchment.widget.adapterview.listview.ListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    MovieAPI api;

    @Bind(R.id.backdrop_movie_image)
    ImageView movieBackdrop;
    @Bind(R.id.favourite_icon)
    Button favouriteIcon;
    @Bind(R.id.movie_title_year)
    TextView movieTitleAndYear;
    @Bind(R.id.movie_duration_genre)
    TextView movieDurationAndGenre;
    @Bind(R.id.movie_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_overview)
    TextView movieOverview;
    @Bind(R.id.movie_video_link)
    ImageView movieVideo;
    @Bind(R.id.movie_rating_bar)
    RatingBar movieRatingBar;
    @Bind(R.id.movie_rating_number)
    TextView movieRatingNumber;
    @Bind(R.id.movie_total_votes)
    TextView movieTotalVotes;
    @Bind(R.id.movie_reviews_link)
    ImageView movieReviewsLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        api = RestService.get();
        ButterKnife.bind(MovieActivity.this);


        //final ImageView movieBackdrop = (ImageView) this.findViewById(R.id.backdrop_movie_image);
        Intent intent = getIntent();
        long movieId = intent.getLongExtra(Constants.INTENT_KEY, 1);
        int tvShow = intent.getIntExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, -1);

        if (tvShow == -1) {
            showMovie(movieId);
        } else {
            showTVShow(movieId);
        }


    }


    private void showMovie(final long movieId) {

        api.findSingleMovie(movieId, new Callback<Movie>() {

                    @Override
                    public void success(Movie movie, Response response) {

                        String backdropPath = MovieUtils.getBackdropURL(Constants.BACKDROP_SIZE_W1280, movie);

                        if (backdropPath == null) {
                            Picasso.with(MovieActivity.this).load(R.drawable.default_backdrop).into(movieBackdrop);
                        } else {
                            Picasso.with(MovieActivity.this).load(backdropPath).into(movieBackdrop);
                        }

                        if (movie.isFavourite()) {
                            favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
                        }
                        movieTitleAndYear.setText(MovieUtils.getTitleWithYear(movie, Constants.MOVIE));
                        movieDurationAndGenre.setText(MovieUtils.getDurationAndGenre(movie));

                        Picasso.with(MovieActivity.this).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, movie)).into(moviePoster);
                        if (movie.getOverview().length() > 250) {
                            movieOverview.setText(MovieUtils.getShorterOverview(movie));
                        } else {
                            movieOverview.setText(movie.getOverview());
                        }
                        movieRatingBar.setRating((float) movie.getVoteAverage() / 2);
                        movieRatingNumber.setText(String.valueOf(movie.getVoteAverage()));
                        movieTotalVotes.setText(String.valueOf(movie.getVoteCount()));

                        movieReviewsLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MOVIE_REVIEW_URL_BASE + movieId + Constants.MOVIE_REVIEW_URL_EXTRA)));
                            }
                        });

                        api.findMovieVideo(movieId, new Callback<Videos>() {

                            @Override
                            public void success(Videos videos, Response response) {
                                final String trailerKey = MovieUtils.getMovieTrailer(videos);
                                if (trailerKey == null) {
                                    movieVideo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(MovieActivity.this, "No YouTube trailer for this movie", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    movieVideo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + trailerKey)));
                                        }
                                    });
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, "Failed to load movie video", error);
                            }
                        });

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_TAG, "Failed to load single movie", error);

                    }
                }

        );

        api.findMovieCast(movieId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                CreditsAdapter creditsAdapter = new CreditsAdapter(credits, 1);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.movie_cast_list);
                horizontalListView.setAdapter(creditsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie credits", error);
            }
        });

        api.findMovieBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(Images images, Response response) {
                ImageAdapter imageAdapter = new ImageAdapter(images, 1);
                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.movie_backdrop_list);
                horizontalListView.setAdapter(imageAdapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    private void showTVShow(long movieId) {
        api.findSingleTvShow(movieId, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                String backdropPath = MovieUtils.getBackdropURL(Constants.BACKDROP_SIZE_W1280, movie);

                if (backdropPath == null) {
                    Picasso.with(MovieActivity.this).load(R.drawable.default_backdrop).into(movieBackdrop);
                } else {
                    Picasso.with(MovieActivity.this).load(backdropPath).into(movieBackdrop);
                }
                movieTitleAndYear.setText(MovieUtils.getTitleWithYear(movie, Constants.TV_SHOWS));

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load single tv show", error);
            }
        });
    }

}
