package com.atlantbh.boristomic.movieapplication.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.DrawerAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.SlideshowPagerAdapter;
import com.atlantbh.boristomic.movieapplication.listeners.ActorClicked;
import com.atlantbh.boristomic.movieapplication.listeners.DrawerMenuItemClicked;
import com.atlantbh.boristomic.movieapplication.listeners.MovieReviewClicked;
import com.atlantbh.boristomic.movieapplication.listeners.MovieVideosClicked;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;
import com.atlantbh.boristomic.movieapplication.models.rest.Cast;
import com.atlantbh.boristomic.movieapplication.models.rest.Credits;
import com.atlantbh.boristomic.movieapplication.models.rest.Images;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.models.rest.Videos;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Connection;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.atlantbh.boristomic.movieapplication.utils.PosterLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] drawerItemTitles;
    private TypedArray drawerItemIcons;
    private List<DrawerItem> drawerItems;
    private DrawerAdapter drawerAdapter;

    private MovieAPI api;

    @Bind(R.id.favourite_icon)
    protected ImageView favouriteIcon;
    @Bind(R.id.movie_title_year)
    protected TextView movieTitleAndYear;
    @Bind(R.id.movie_duration_genre)
    protected TextView movieDurationAndGenre;
    @Bind(R.id.movie_poster)
    protected ImageView moviePoster;
    @Bind(R.id.movie_overview)
    protected TextView movieOverview;
    @Bind(R.id.movie_video_link)
    protected ImageView movieVideo;
    @Bind(R.id.movie_rating_bar)
    protected RatingBar movieRatingBar;
    @Bind(R.id.movie_rating_number)
    protected TextView movieRatingNumber;
    @Bind(R.id.movie_total_votes)
    protected TextView movieTotalVotes;
    @Bind(R.id.movie_reviews_link)
    protected ImageView movieReviewsLink;
    @Bind(R.id.view_pager_backdrop_image)
    protected ViewPager viewPager;
    @Bind(R.id.default_backdrop_image)
    protected ImageView defaultBackdrop;
    @Bind(R.id.top_billed_actors_layout)
    protected LinearLayout topBilledCast;

    private Toolbar toolbar;
    private Realm realm;
    private PosterLoader target;

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
        ButterKnife.bind(MovieActivity.this);
        target = new PosterLoader(moviePoster, toolbar);

        realm = Realm.getInstance(this);

        final Intent intent = getIntent();
        final long movieId = intent.getLongExtra(Constants.INTENT_KEY, -1);
        final int tvShow = intent.getIntExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, -1);

        drawerItemTitles = getResources().getStringArray(R.array.drawer_menu_titles);
        drawerItemIcons = getResources().obtainTypedArray(R.array.drawer_menu_icons);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(drawerItemTitles[0], drawerItemIcons.getResourceId(0, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[1], drawerItemIcons.getResourceId(1, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[2], drawerItemIcons.getResourceId(2, -1)));
        drawerItemIcons.recycle();
        drawerAdapter = new DrawerAdapter(drawerItems);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new DrawerMenuItemClicked(drawerLayout, getBaseContext()));

        if (!Connection.checkNetworkConnection(this)) {
            populateMovieDataLight(movieId, tvShow);
        } else {

            api = RestService.get();

            if (tvShow == -1) {
                showMovie(movieId);
            } else {
                showTVShow(movieId);
            }
        }
    }

    private void populateMovieDataLight(long movieId, int tvShow) {
        MovieDB movie = MovieDB.findMovieById(realm, movieId);
        if (tvShow == -1) {
            toolbar.setTitle(movie.getTitle());
            movieTitleAndYear.setText(movie.getTitle() + " (" + movie.getReleaseDate() + ")");
            movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.MOVIE));

        } else {
            toolbar.setTitle(movie.getName());
            movieTitleAndYear.setText(movie.getName() + " (" + movie.getReleaseDate() + ")");
            movieReviewsLink.setOnClickListener(new MovieReviewClicked(movieId, MovieActivity.this, Constants.TV_SHOWS));

        }
        movieDurationAndGenre.setText(movie.getRuntime() + " | " + movie.getGenres());
        Picasso.with(MovieActivity.this).load(R.drawable.poster_default).into(moviePoster);
        if (movie.getOverview().length() > 250) {
            movieOverview.setText(movie.getOverview().substring(0, 220) + "...");
        } else {
            movieOverview.setText(movie.getOverview());
        }
        movieRatingBar.setRating(movie.getVote());
        movieRatingNumber.setText(movie.getVoteAverage());
        movieTotalVotes.setText(String.valueOf(movie.getVoteCount()));
        setMovieFavourite(movieId, null);
        defaultBackdrop.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        Picasso.with(MovieActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
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
                for (int i = 0; i < 3; i++) {
                    setTopBilledCast(credits.getCast(), i);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie credits", error);
            }
        });

        api.findMovieBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(Images images, Response response) {
                if (images.getBackdrops().size() == 0) {
                    defaultBackdrop.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    Picasso.with(MovieActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
                } else {
                    SlideshowPagerAdapter slideshowPagerAdapter = new SlideshowPagerAdapter(MovieActivity.this, images.getBackdrops());
                    viewPager.setAdapter(slideshowPagerAdapter);
                }
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
                for (int i = 0; i < 3; i++) {
                    setTopBilledCast(credits.getCast(), i);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load tv show credits", error);
            }
        });

        api.findTvShowBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(Images images, Response response) {
                if (images.getBackdrops().size() == 0) {
                    defaultBackdrop.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    Picasso.with(MovieActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
                } else {
                    SlideshowPagerAdapter slideshowPagerAdapter = new SlideshowPagerAdapter(MovieActivity.this, images.getBackdrops());
                    viewPager.setAdapter(slideshowPagerAdapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load tv show backdrops", error);

            }
        });
    }

    /**
     * Populates movie data with information from api, calls different methods that do the job
     *
     * @param movie   <code>Movie</code> type value of movie
     * @param type    <code>int</code> type value of movie or tv show type
     * @param movieId <code>long</code> type value of movie id or tv show id
     */
    private void populateMovieData(final Movie movie, final int type, final long movieId) {
        setToolbarTitle(movie, type);
        setMovieTitleAndYear(movie, type);
        setMovieReviewsLink(movie, type, movieId);
        setMovieDurationAndGenre(movie);
        setMoviePosterImage(movie);
        setMovieOverview(movie);
        setMovieRatingBar(movie);
        setMovieTotalRatingNumberAndVoteCount(movie);
        setMovieFavourite(movieId, movie);

    }

    private void setMovieFavourite(final long movieId, final Movie movie) {
        final MovieDB movieDB = MovieDB.findMovieById(realm, movieId);

        if (movieDB != null) {
            if (movieDB.isFavourite()) {
                favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
            }
        }

        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDB movieToSave = MovieDB.findMovieById(realm, movieId);
                if (movieToSave != null) {
                    if (MovieDB.updateMovieFavourite(realm, movieToSave)) {
                        favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
                    } else {
                        favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_movie);
                    }
                } else {
                    if (MovieDB.saveNewMovie(realm, movie)) {
                        favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
                    } else {
                        Toast.makeText(MovieActivity.this, "Failed to save movie to database", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     * Sets rating number and total votes count of a movie or tv show
     *
     * @param movie movie <code>Movie</code> type value of movie
     */
    private void setMovieTotalRatingNumberAndVoteCount(Movie movie) {
        movieRatingNumber.setText(MovieUtils.getMovieStringRating(movie));
        movieTotalVotes.setText(String.valueOf(movie.getVoteCount()));
    }

    /**
     * Sets movie rating bar, movie rating comes from api in number up to 10 max,
     * 5 star rating bar is used so rating is cut in half to display it properly.
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMovieRatingBar(Movie movie) {
        movieRatingBar.setRating(MovieUtils.getMovieFloatRating(movie));
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
        if (movie.getPosterPath() == null) {
            Picasso.with(MovieActivity.this).load(R.drawable.poster_default).into(moviePoster);
        } else {
            Picasso.with(MovieActivity.this).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, movie)).into(target);
        }
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

    private void setTopBilledCast(List<Cast> casts, int position) {
        if (casts != null || casts.size() != 0) {
            if (position < casts.size()) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                params.setMargins(13, 13, 13, 13);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(params);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setWeightSum(4.0f);

                ImageView castImage = new ImageView(this);
                castImage.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.6f));
                castImage.setScaleType(ImageView.ScaleType.FIT_XY);

                if (casts.get(position).getProfilePath() == null) {
                    Picasso.with(this).load(R.drawable.profile_default).into(castImage);
                } else {
                    Picasso.with(this).load(MovieUtils.getCastImageURL(Constants.PROFILE_SIZE_W185, casts.get(position))).into(castImage);
                }

                TextView castName = new TextView(this);
                castName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.4f));
                castName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                castName.setText(casts.get(position).getName());

                linearLayout.addView(castImage);
                linearLayout.addView(castName);
                linearLayout.setOnClickListener(new ActorClicked(casts.get(position), this));

                topBilledCast.addView(linearLayout);
            }
        } else {
            topBilledCast.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        super.onDestroy();
    }
}
