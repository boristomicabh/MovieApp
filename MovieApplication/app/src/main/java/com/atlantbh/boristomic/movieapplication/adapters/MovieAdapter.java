package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.holders.MovieHolder;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;

import java.util.List;

/**
 * Created by boristomic on 28/01/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

    private List<Movie> movies;
    private Context context;
    private int listType;

    /**
     * Public constructor of MovieAdapter used to get list of movies so it can be used to setup RecyclerView.
     * Initializes context so it can be passed to MovieHolder for action events, and to setup inflater.
     * And list type so MovieHolder can setup appropriate parameters.
     *
     * @param movies   <code>List</code> type object of movies
     * @param context  <code>Context</code> type object of activity called from
     * @param listType <code>int</code> type object of list type
     */
    public MovieAdapter(List<Movie> movies, Context context, int listType) {
        this.movies = movies;
        this.context = context;
        this.listType = listType;
    }

    /**
     * Creates LayoutInflater from context, and then inflates movie_list_view.xml layout.
     * Then creates new MovieHolder with inflated view, and passes context for event action
     * and listType.
     *
     * @param parent   <code>ViewGroup</code> type object of parent view
     * @param viewType <code>int</code> type value of viewType
     * @return <code>MovieHolder</code> type object
     */
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_view, parent, false);
        return new MovieHolder(view, context, listType);
    }

    /**
     * Binds <code>Movie</code> type object to new element in RecyclerView.
     *
     * @param holder   <code>MovieHolder</code> type object
     * @param position <code>int</code> type value of <code>Movie</code> position in list of movies
     */
    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.bindMovie(movie);
    }

    /**
     * Returns size of movie list, total count of elements
     *
     * @return <code>int</code> type value of movie list size
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }

}
