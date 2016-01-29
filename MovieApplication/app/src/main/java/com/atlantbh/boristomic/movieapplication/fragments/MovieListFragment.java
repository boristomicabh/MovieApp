package com.atlantbh.boristomic.movieapplication.fragments;

import android.net.Uri;
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
import com.atlantbh.boristomic.movieapplication.models.MoviesResponse;
import com.atlantbh.boristomic.movieapplication.services.RestService;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieListFragment extends Fragment {

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();

    private int listType;

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

    /**
     * Retrieves list type from bundle
     *
     * @param savedInstanceState <code>Bundle</code> type object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (Constants.MOST_POPULAR_MOVIES_LIST == listType) {
            populatePopularMovies(fragmentView);
        } else if (Constants.TOP_RATED_MOVIES_LIST == listType) {
            populateTopRatedMovies(fragmentView);
        } else if (Constants.UPCOMING_MOVIES_LIST == listType) {
            populateUpcomingMovies(fragmentView);
        } else if (Constants.MOST_POPULAR_TV_SHOWS_LIST == listType) {
            populatePopularTvShows(fragmentView);
        } else {
            populateTopRatedTvShows(fragmentView);
        }
        return fragmentView;
    }

    /**
     * Calls api method that finds all popular movies, and then sets them into a list
     */
    private void populatePopularMovies(final View fragmentView) {
        RestService.get().listPopularMovies(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                //final MovieAdapterOld movieAdapter = new MovieAdapterOld(moviesResponse.getResults(), null, null, Constants.OTHER_LISTS, 800);
                final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MovieAdapter(moviesResponse.getResults(), getActivity().getBaseContext(), Constants.OTHER_LISTS));
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
    private void populateTopRatedMovies(final View fragmentView) {
        RestService.get().listTopRatedMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MovieAdapter(moviesResponse.getResults(), getActivity().getBaseContext(), Constants.OTHER_LISTS));
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
    private void populateUpcomingMovies(final View fragmentView) {
        RestService.get().listUpcomingMovies(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MovieAdapter(moviesResponse.getResults(), getActivity().getBaseContext(), Constants.UPCOMING_MOVIES));
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
    private void populatePopularTvShows(final View fragmentView) {
        RestService.get().listPopularTvShows(Constants.QUERY_POPULARITY_DESC, new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MovieAdapter(moviesResponse.getResults(), getActivity().getBaseContext(), Constants.TV_SHOWS));
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
    private void populateTopRatedTvShows(final View fragmentView) {
        RestService.get().listTopRatedTvShows(new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.movies_feed_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new MovieAdapter(moviesResponse.getResults(), getActivity().getBaseContext(), Constants.TV_SHOWS));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed to get list of top rated TV Shows from API", error);
            }
        });
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
