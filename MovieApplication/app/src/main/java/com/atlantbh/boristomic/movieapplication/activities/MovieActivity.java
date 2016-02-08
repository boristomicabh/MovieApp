package com.atlantbh.boristomic.movieapplication.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.DrawerAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.HorizontalListAdapter;
import com.atlantbh.boristomic.movieapplication.adapters.SlideshowPagerAdapter;
import com.atlantbh.boristomic.movieapplication.listeners.DrawerMenuItemClicked;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;
import com.atlantbh.boristomic.movieapplication.models.rest.Credits;
import com.atlantbh.boristomic.movieapplication.models.rest.Images;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.models.rest.MovieReviews;
import com.atlantbh.boristomic.movieapplication.models.rest.Review;
import com.atlantbh.boristomic.movieapplication.models.rest.Trailer;
import com.atlantbh.boristomic.movieapplication.models.rest.Videos;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Connection;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.atlantbh.boristomic.movieapplication.utils.PosterLoader;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieActivity.class.getSimpleName();

    private Toolbar toolbar;
    private MovieAPI api;
    private Realm realm;
    private PosterLoader target;
    private static Context context;

    @Bind(R.id.favourite_icon)
    protected ImageView favouriteIcon;
    @Bind(R.id.movie_title_year)
    protected TextView movieTitleAndYear;
    @Bind(R.id.movie_duration_genre)
    protected TextView movieDurationAndGenre;
    @Bind(R.id.movie_overview)
    protected TextView movieOverview;
    @Bind(R.id.movie_rating_bar)
    protected RatingBar movieRatingBar;
    @Bind(R.id.movie_rating_number)
    protected TextView movieRatingNumber;
    @Bind(R.id.view_pager_backdrop_image)
    protected ViewPager viewPager;
    @Bind(R.id.default_backdrop_image)
    protected ImageView defaultBackdrop;
    @Bind(R.id.movie_cast_list)
    protected RecyclerView castList;
    @Bind(R.id.pager_indicator)
    protected CirclePageIndicator indicator;
    @Bind(R.id.reviews_layout)
    protected LinearLayout reviewsLayout;
    @Bind(R.id.view_pager_layout)
    protected RelativeLayout viewPagerLayout;
    @Bind(R.id.user_rating_title)
    protected TextView userRating;

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
        setupDrawer();
        target = new PosterLoader(toolbar, indicator);
        realm = Realm.getInstance(this);
        context = this;

        final Intent intent = getIntent();
        final long movieId = intent.getLongExtra(Constants.INTENT_KEY, -1);
        final int tvShow = intent.getIntExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, -1);

        if (!Connection.isConnected(this)) {
            populateMovieDataLight(movieId, tvShow);
        } else {

            api = RestService.get();

            if (tvShow == -1) {
                showMovie(movieId, Constants.MOVIE);
            } else {
                showTVShow(movieId, Constants.TV_SHOWS);
            }
        }
    }

    /**
     * Setups drawer list with titles and icons, also adds a listener to each item
     */
    private void setupDrawer() {
        String[] drawerItemTitles = getResources().getStringArray(R.array.drawer_menu_titles);
        TypedArray drawerItemIcons = getResources().obtainTypedArray(R.array.drawer_menu_icons);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.drawer_list);
        List<DrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(drawerItemTitles[0], drawerItemIcons.getResourceId(0, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[1], drawerItemIcons.getResourceId(1, -1)));
        drawerItems.add(new DrawerItem(drawerItemTitles[2], drawerItemIcons.getResourceId(2, -1)));
        drawerItemIcons.recycle();
        DrawerAdapter drawerAdapter = new DrawerAdapter(drawerItems);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new DrawerMenuItemClicked(drawerLayout, getBaseContext()));
    }

    /**
     * Populates movie data from database when there is no connection
     *
     * @param movieId <code>long</code> value of movie id
     * @param tvShow  <colde>int</colde> value if item is tv show
     */
    private void populateMovieDataLight(long movieId, int tvShow) {
        MovieDB movie = MovieDB.findMovieById(realm, movieId);
        if (tvShow == -1) {
            toolbar.setTitle(movie.getTitle());
            movieTitleAndYear.setText(movie.getTitle() + " (" + movie.getReleaseDate() + ")");
        } else {
            toolbar.setTitle(movie.getName());
            movieTitleAndYear.setText(movie.getName() + " (" + movie.getReleaseDate() + ")");
        }
        movieDurationAndGenre.setText(movie.getRuntime() + " | " + movie.getGenres());
        if (movie.getOverview().length() > 250) {
            movieOverview.setText(movie.getOverview().substring(0, 220) + "...");
        } else {
            movieOverview.setText(movie.getOverview());
        }
        movieRatingBar.setRating(movie.getVote());
        movieRatingNumber.setText(movie.getVoteAverage());
        if (movie.getMyRating() > 0) {
            userRating.setText("My Rating " + String.format("%.1f", movie.getMyRating() / 2));
        }
        setMovieFavourite(movieId, null);
        defaultBackdrop.setVisibility(View.VISIBLE);
        viewPagerLayout.setVisibility(View.GONE);
        Picasso.with(MovieActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
    }

    /**
     * Makes multiple queries to api and populates view with movie data.
     * Queries called are for finding single movie, movie trailers, movie cast and movie backdrops
     *
     * @param movieId <code>long</code> type value of movie id
     */
    private void showMovie(final long movieId, final int type) {

        api.findSingleMovie(movieId, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                populateMovieData(movie, Constants.MOVIE, movieId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load single movie", error);
            }
        });


        /**
         * Finds cast of movie and populates data in horizontal recycler view
         */
        api.findMovieCast(movieId, new Callback<Credits>() {
            @Override
            public void success(Credits credits, Response response) {
                setCast(credits);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie credits", error);
            }
        });

        /**
         * Finds movie images and if successful finds youtube trailer
         */
        api.findMovieBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(final Images images, Response response) {
                setBackdropsWithTrailer(images, movieId, type);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie backdrops", error);
            }
        });

        /**
         * Finds movie reviews
         */
        api.findMovieReviews(movieId, new Callback<MovieReviews>() {
            @Override
            public void success(MovieReviews movieReviews, Response response) {
                setReviews(movieReviews.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load movie reviews", error);
            }
        });

    }

    private void setCast(Credits credits) {
        castList.setLayoutManager(new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.HORIZONTAL, false));
        castList.setAdapter(new HorizontalListAdapter(credits.getCast(), Constants.CAST, MovieActivity.this));
    }

    /**
     * Makes multiple queries to api and populates view with tv show data.
     * Queries called are for finding single tv show, tv show trailers, tv show cast and tv show backdrops
     *
     * @param movieId <code>long</code> type value of tv show id
     */
    private void showTVShow(final long movieId, final int type) {

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

        /**
         * Finds cast of tv show and populates data in horizontal recycler view
         */
        api.findTvShowCast(movieId, new Callback<Credits>() {

            @Override
            public void success(Credits credits, Response response) {
                setCast(credits);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to load tv show credits", error);
            }
        });

        api.findTvShowBackdrops(movieId, new Callback<Images>() {

            @Override
            public void success(Images images, Response response) {
                setBackdropsWithTrailer(images, movieId, type);
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
        setMovieDurationAndGenre(movie);
        setMoviePosterImage(movie);
        setMovieOverview(movie);
        setMovieRatingBar(movie);
        setMovieTotalRatingNumberAndVoteCount(movie);
        setMovieFavourite(movieId, movie);

        userRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(movie);
            }
        });

    }

    public void show(final Movie movie) {
        final Dialog d = new Dialog(MovieActivity.this);
        d.setTitle("Rate " + toolbar.getTitle());
        d.setContentView(R.layout.rating_dialog);
        final Button rate = (Button) d.findViewById(R.id.rate_button);
        final Button cancel = (Button) d.findViewById(R.id.cancel_button);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);
        np.setMaxValue(10);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userRating.setText("My Rating " + String.format("%.1f", (float) np.getValue() / 2));
                MovieDB temp = MovieDB.findMovieById(realm, movie.getId());
                if (temp == null) {
                    MovieDB.SaveNewMoveOnRating(realm, movie, np.getValue());
                } else {
                    MovieDB.updateMovieRating(realm, temp, np.getValue());
                }
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void setMovieFavourite(final long movieId, final Movie movie) {
        final MovieDB movieDB = MovieDB.findMovieById(realm, movieId);

        if (movieDB != null) {
            if (movieDB.isFavourite()) {
                favouriteIcon.setBackgroundResource(R.drawable.ic_favourite_liked);
            }
            if (movieDB.getMyRating() != 0) {
                userRating.setText("My Rating " + String.format("%.1f", movieDB.getMyRating() / 2));
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
        //movieTotalVotes.setText(String.valueOf(movie.getVoteCount()));
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
        movieOverview.setText(movie.getOverview());
        movieOverview.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = true;

            @Override
            public void onClick(View v) {
                if (isClicked) {
                    movieOverview.setMaxLines(Integer.MAX_VALUE);
                    isClicked = false;
                } else {
                    movieOverview.setMaxLines(3);
                    isClicked = true;
                }
            }
        });
    }

    /**
     * Sets image poster
     *
     * @param movie <code>Movie</code> type value of movie
     */
    private void setMoviePosterImage(Movie movie) {
        Picasso.with(MovieActivity.this).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, movie)).into(target);
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

    private void setReviews(List<Review> reviews) {
        for (Review r : reviews) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 6, 0, 0);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView authorName = new TextView(this);
            authorName.setTextColor(getResources().getColor(R.color.textHighlight));
            authorName.setTextSize(14f);
            authorName.setText(r.getAuthor());

            TextView reviewContent = new TextView(this);
            reviewContent.setPadding(0, 10, 0, 0);
            reviewContent.setTextColor(getResources().getColor(R.color.textLighter));
            reviewContent.setTextSize(12f);
            reviewContent.setText(r.getContent());

            linearLayout.addView(authorName);
            linearLayout.addView(reviewContent);
            reviewsLayout.addView(linearLayout);
        }
    }

    private void setBackdropsWithTrailer(final Images images, final long movieId, final int type) {
        if (images.getBackdrops().size() == 0) {
            defaultBackdrop.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            Picasso.with(MovieActivity.this).load(R.drawable.backdrop_default).into(defaultBackdrop);
        } else {
            findTrailers(images, movieId, type);
        }
    }

    private void findTrailers(final Images images, final long movieId, final int type) {
        final List<Object> objects = new ArrayList<>();
        if (type == Constants.MOVIE) {
            api.findMovieVideo(movieId, new Callback<Videos>() {

                @Override
                public void success(Videos videos, Response response) {
                    Trailer t = MovieUtils.getMovieYouTubeTrailer(videos);
                    if (t != null) {
                        objects.add(t);
                    }
                    objects.addAll(images.getBackdrops());
                    setupPagerWithBackdrops(objects);
                }

                @Override
                public void failure(RetrofitError error) {
                    objects.addAll(images.getBackdrops());
                    setupPagerWithBackdrops(objects);
                    Log.e(LOG_TAG, "Failed to load movie video", error);
                }
            });
        } else if (type == Constants.TV_SHOWS) {
            api.findTvShowVideo(movieId, new Callback<Videos>() {
                @Override
                public void success(Videos videos, Response response) {
                    Trailer t = MovieUtils.getMovieYouTubeTrailer(videos);
                    if (t != null) {
                        objects.add(t);
                    }
                    objects.addAll(images.getBackdrops());
                    setupPagerWithBackdrops(objects);
                }

                @Override
                public void failure(RetrofitError error) {
                    objects.addAll(images.getBackdrops());
                    setupPagerWithBackdrops(objects);
                    Log.e(LOG_TAG, "Failed to load movie video", error);
                }
            });
        }
    }

    private void setupPagerWithBackdrops(List<Object> objects) {
        SlideshowPagerAdapter slideshowPagerAdapter = new SlideshowPagerAdapter(MovieActivity.this, objects, getSupportFragmentManager());
        viewPager.setAdapter(slideshowPagerAdapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        super.onDestroy();
    }

    /**
     * Returns context for static use
     *
     * @return
     */
    public static Context getContext() {
        return context;
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
}
