package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

    private Toolbar toolbar;

    /**
     * Sets layout activity_movie.xml, toolbar, rest service and butter knife to find all views.
     * Movie id or tv show is taken from intent and shown if id is valid (invalid is -1)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = RestService.get();
        ButterKnife.bind(MovieActivity.this);

        final Intent intent = getIntent();
        final long movieId = intent.getLongExtra(Constants.INTENT_KEY, -1);
        final int tvShow = intent.getIntExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, -1);

        if (tvShow == -1) {
            showMovie(movieId);
        } else {
            showTVShow(movieId);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Makes multiple queries to api and populates view with movie data.
     * Queries called are for finding single movie, movie trailers, movie cast and movie backdrops
     *
     * @param movieId <code>long</code> type value of movie id
     */
    private void showMovie(final long movieId) {

        api.findSingleMovie(movieId, new Callback<Movie>() {

                    @Override
                    public void success(Movie movie, Response response) {
                        populateMovieData(movie, Constants.MOVIE, movieId);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_TAG, "Failed to load single movie", error);
                    }
                }
        );

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

    /**
     * Makes multiple queries to api and populates view with tv show data.
     * Queries called are for finding single tv show, tv show trailers, tv show cast and tv show backdrops
     *
     * @param movieId <code>long</code> type value of tv show id
     */
    private void showTVShow(final long movieId) {

        api.findSingleTvShow(movieId, new Callback<Movie>() {

            @Override
            public void success(Movie movie, Response response) {
                populateMovieData(movie, Constants.TV_SHOWS, movieId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load single tv show", error);
            }
        });

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

    // TODO break into smaller methods
    private void populateMovieData(final Movie movie, final int type, final long movieId) {
        setToolbarTitle(movie, type);
        setMovieTitleAndYear(movie, type);
        setMovieReviewsLink(movie, type, movieId);
        setMovieBackdropImage(movie);
        setMovieDurationAndGenre(movie);
        setMoviePosterImage(movie);
        setMovieOverview(movie);
        setMovieRatingBar(movie);
        setMovieTotalRatingNumberAndVoteCount(movie);

        if (movie.isFavourite()) {
            favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
        }
    }

    /**
     * Sets rating number and total votes count of a movie or tv show
     *
     * @param movie movie <code>Movie</code> type value of movie
     */
    private void setMovieTotalRatingNumberAndVoteCount(Movie movie) {
        movieRatingNumber.setText(String.valueOf(movie.getVoteAverage()));
        movieTotalVotes.setText(String.valueOf(movie.getVoteCount()));
    }

    /**
     * Sets movie rating bar, movie rating comes from api in number up to 10 max,
     * 5 star rating bar is used so rating is cut in half to display it properly.
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMovieRatingBar(Movie movie) {
        movieRatingBar.setRating((float) movie.getVoteAverage() / 2);
    }

    /**
     * Sets movie overview, if overview is longer then 250 characters, shorted version is displayed
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMovieOverview(Movie movie) {
        if (movie.getOverview().length() > 250) {
            movieOverview.setText(MovieUtils.getShorterOverview(movie));
        } else {
            movieOverview.setText(movie.getOverview());
        }
    }

    /**
     * Sets image poster
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMoviePosterImage(Movie movie) {
        Picasso.with(MovieActivity.this).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W185, movie)).resize(342, 513).into(moviePoster);
    }

    /**
     * Sets duration of a movie and it's genre
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMovieDurationAndGenre(Movie movie) {
        movieDurationAndGenre.setText(MovieUtils.getDurationAndGenre(movie));
    }

    /**
     * Sets image on top of view, if image is not found default one is set.
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMovieBackdropImage(final Movie movie) {
        String backdropPath = MovieUtils.getBackdropURL(Constants.BACKDROP_SIZE_W500, movie);

        if (backdropPath == null) {
            Picasso.with(MovieActivity.this).load(R.drawable.default_backdrop).into(movieBackdrop);
        } else {
            Picasso.with(MovieActivity.this).load(backdropPath).resize(1280, 720).into(movieBackdrop);
        }
    }

    /**
     * Sets link to movie reviews or tv show info page
     *
     * @param movie   <code>Movie</code> type value of movie
     * @param type    <code>int</code> type value of movie or tv show type
     * @param movieId <code>long</code> type value of movie id or tv show id
     */
    private void setMovieReviewsLink(final Movie movie, final int type, final long movieId) {
        if (type == Constants.MOVIE) {
            movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.MOVIE));
        } else {
            movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.TV_SHOWS));
        }
    }

    /**
     * Sets title and year of movie or tv show in default format
     *
     * @param movie <code>Movie</code> type value of movie
     * @param type  <code>int</code> type value of movie or tv show type
     */
    private void setMovieTitleAndYear(final Movie movie, final int type) {
        if (type == Constants.MOVIE) {
            movieTitleAndYear.setText(MovieUtils.getTitleWithYear(movie, Constants.MOVIE));
        } else {
            movieTitleAndYear.setText(MovieUtils.getTitleWithYear(movie, Constants.TV_SHOWS));
        }
    }

    /**
     * Sets title of toolbar same as title of tv show or movie
     *
     * @param movie <code>Movie</code> type value of movie
     * @param type  <code>int</code> type value of movie or tv show type
     */
    private void setToolbarTitle(final Movie movie, final int type) {
        if (type == Constants.MOVIE) {
            toolbar.setTitle(movie.getTitle());
        } else {
            toolbar.setTitle(movie.getName());
        }
    }

}
