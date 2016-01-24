package com.atlantbh.boristomic.movieapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.MovieAdapter;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.models.MoviesResponse;
import com.atlantbh.boristomic.movieapplication.services.MovieAPI;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import java.util.List;

import mobi.parchment.widget.adapterview.listview.ListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static Context context;

    private List<Movie> popularMovies = null;
    private List<Movie> topRatedMovies = null;
    private List<Movie> upcomingMovies = null;
    private List<Movie> topRatedTvShows = null;
    private List<Movie> popularTvShows = null;

    private MovieAdapter popularMoviesAdapter;
    private MovieAdapter topRatedMoviesAdapter;
    private MovieAdapter upcomingMoviesAdapter;
    private MovieAdapter topRatedTvShowsAdapter;
    private MovieAdapter popularTvShowsAdapter;

    private MovieAPI api = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = RestService.get();
        populatePopularMovies();
        populateTopRatedMovies();
        populateUpcomingMovies();
        populateTopRatedTvShows();
        populatePopularTvShows();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_movie);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Log.i("onQueryTextChange", newText);

                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.i("onQueryTextSubmit", query);

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);



        return super.onCreateOptionsMenu(menu);
    }

    private void populatePopularMovies() {
        api.listPopularMovies(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {

            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                popularMovies = moviesResponse.getResults();
                popularMoviesAdapter = new MovieAdapter(popularMovies, null, null, Constants.OTHER_LISTS);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.most_popular_movies_list);
                horizontalListView.setAdapter(popularMoviesAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of popular movies from API", error);
            }
        });

    }

    private void populateTopRatedMovies() {
        api.listTopRatedMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                topRatedMovies = moviesResponse.getResults();
                topRatedMoviesAdapter = new MovieAdapter(topRatedMovies, null, null, Constants.OTHER_LISTS);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.top_rated_movies_list);
                horizontalListView.setAdapter(topRatedMoviesAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of top rated movies from API", error);
            }
        });
    }

    private void populateUpcomingMovies() {
        api.listUpcomingMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                upcomingMovies = moviesResponse.getResults();
                upcomingMoviesAdapter = new MovieAdapter(upcomingMovies, null, null, Constants.UPCOMING_MOVIES);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.upcoming_movies_list);
                horizontalListView.setAdapter(upcomingMoviesAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of upcoming movies from API", error);
            }
        });
    }

    private void populateTopRatedTvShows() {
        api.listTopRatedTvShows(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                topRatedTvShows = moviesResponse.getResults();
                topRatedTvShowsAdapter = new MovieAdapter(topRatedTvShows, null, null, Constants.TV_SHOWS);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.top_rated_tvshows_list);
                horizontalListView.setAdapter(topRatedTvShowsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of top rated TV Shows from API", error);
            }
        });
    }

    private void populatePopularTvShows() {
        api.listPopularTvShows(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {

            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                popularTvShows = moviesResponse.getResults();
                popularTvShowsAdapter = new MovieAdapter(popularTvShows, null, null, Constants.TV_SHOWS);

                final ListView horizontalListView = (ListView<BaseAdapter>) findViewById(R.id.most_popular_tvshows_list);
                horizontalListView.setAdapter(popularTvShowsAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of popular TV Shows from API", error);
            }
        });

    }

    public static Context getContext() {
        return context;
    }


}

