package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.MovieAdapter;
import com.atlantbh.boristomic.movieapplication.listeners.MovieReviewClicked;
import com.atlantbh.boristomic.movieapplication.listeners.MovieVideosClicked;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.models.Videos;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.parchment.widget.adapterview.listview.ListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MovieAPI api;

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

        Intent intent = getIntent();
        final long movieId = intent.getLongExtra(Constants.INTENT_KEY, 1);
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
                        movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.MOVIE));

                        api.findMovieVideo(movieId, new Callback<Videos>() {

                            @Override
                            public void success(Videos videos, Response response) {
                                final String trailerKey = MovieUtils.getMovieTrailer(videos);
                                movieVideo.setOnClickListener(new MovieVideosClicked(trailerKey, MovieActivity.this));
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
                MovieAdapter creditsAdapter = new MovieAdapter(null, null, credits, Constants.CAST);

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
                MovieAdapter imageAdapter = new MovieAdapter(null, images, null, Constants.IMAGE);
                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.movie_backdrop_list);
                horizontalListView.setAdapter(imageAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie backdrops", error);
            }
        });


    }

    private void showTVShow(final long movieId) {
        api.findSingleTvShow(movieId, new Callback<Movie>() {
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

                movieTitleAndYear.setText(MovieUtils.getTitleWithYear(movie, Constants.TV_SHOWS));
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

                movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.TV_SHOWS));

                api.findTvShowVideo(movieId, new Callback<Videos>() {

                    @Override
                    public void success(Videos videos, Response response) {
                        final String trailerKey = MovieUtils.getMovieTrailer(videos);
                        movieVideo.setOnClickListener(new MovieVideosClicked(trailerKey, MovieActivity.this));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_TAG, "Failed to load tv show video", error);
                    }
                });


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load single tv show", error);
            }
        });


        api.findTvShowCast(movieId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                MovieAdapter creditsAdapter = new MovieAdapter(null, null, credits, Constants.CAST);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.movie_cast_list);
                horizontalListView.setAdapter(creditsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load tv show credits", error);
            }
        });

        api.findTvShowBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(Images images, Response response) {
                MovieAdapter imageAdapter = new MovieAdapter(null, images, null, Constants.IMAGE);
                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.movie_backdrop_list);
                horizontalListView.setAdapter(imageAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load tv show backdrops", error);

            }
        });
    }

}
