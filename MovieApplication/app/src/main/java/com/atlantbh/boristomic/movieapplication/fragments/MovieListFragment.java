package com.atlantbh.boristomic.movieapplication.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.adapters.MovieAdapter;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.models.rest.MoviesResponse;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieListFragment extends Fragment {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private int listType;
    private List<Movie> movies;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    /**
     * Default empty constructor
     */
    public MovieListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type <code>int</code> type value of list to load
     * @return A new instance of fragment MovieListFragment.
     */
    public static MovieListFragment newInstance(int type) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.FRAGMENT_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    /**
     * Retrieves list type from bundle
     *
     * @param savedInstanceState <code>Bundle</code> type object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        listType = getArguments().getInt(Constants.FRAGMENT_KEY);
    }

    /**
     * Inflates fragment_movie_list.xml layout with appropriate list of movies
     * depending on listType value and returns view.
     *
     * @param inflater           <code>LayoutInflater</code> type object
     * @param container          <code>ViewGroup</code> type object containing fragment view
     * @param savedInstanceState <code>Bundle</code> type object
     * @return inflated <code>View</code> type object with appropriate list of movies
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);

        if (Constants.MOST_POPULAR_MOVIES_LIST == listType) {
            populatePopularMovies();
        } else if (Constants.TOP_RATED_MOVIES_LIST == listType) {
            populateTopRatedMovies();
        } else if (Constants.UPCOMING_MOVIES_LIST == listType) {
            populateUpcomingMovies();
        } else if (Constants.MOST_POPULAR_TV_SHOWS_LIST == listType) {
            populatePopularTvShows();
        } else {
            populateTopRatedTvShows();
        }
        return fragmentView;
    }

    /**
     * Calls api method that finds all popular movies, and then sets them into a list
     */
    private void populatePopularMovies() {
        RestService.get().listPopularMovies(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                movies = moviesResponse.getResults();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieAdapter = new MovieAdapter(movies, getActivity().getBaseContext(), Constants.OTHER_LISTS);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of popular movies from API", error);
            }
        });
    }

    /**
     * Calls api method that finds all top rated movies, and then sets them into a list
     */
    private void populateTopRatedMovies() {
        RestService.get().listTopRatedMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                movies = moviesResponse.getResults();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieAdapter = new MovieAdapter(movies, getActivity().getBaseContext(), Constants.OTHER_LISTS);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of top rated movies from API", error);
            }
        });
    }

    /**
     * Calls api method that finds all upcoming movies, and then sets them into a list
     */
    private void populateUpcomingMovies() {
        RestService.get().listUpcomingMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                movies = moviesResponse.getResults();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieAdapter = new MovieAdapter(movies, getActivity().getBaseContext(), Constants.UPCOMING_MOVIES);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of upcoming movies from API", error);
            }
        });
    }

    /**
     * Calls api method that finds all popular tv shows, and then sets them into a list
     */
    private void populatePopularTvShows() {
        RestService.get().listPopularTvShows(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                movies = moviesResponse.getResults();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieAdapter = new MovieAdapter(movies, getActivity().getBaseContext(), Constants.TV_SHOWS);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of popular TV Shows from API", error);
            }
        });

    }

    /**
     * Calls api method that finds all top rated tv shows, and then sets them into a list
     */
    private void populateTopRatedTvShows() {
        RestService.get().listTopRatedTvShows(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                movies = moviesResponse.getResults();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                movieAdapter = new MovieAdapter(movies, getActivity().getBaseContext(), Constants.TV_SHOWS);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of top rated TV Shows from API", error);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("orijentacija", "landscape");
            updateUI();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateUI();
            Log.v("orijentacija", "portrait");
        }
    }

    private void updateUI() {
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
