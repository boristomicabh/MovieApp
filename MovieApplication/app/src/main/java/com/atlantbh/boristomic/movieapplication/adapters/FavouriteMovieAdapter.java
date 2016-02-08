package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.holders.FavouriteMovieHolder;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;

import java.util.List;

/**
 * Created by boristomic on 30/01/16.
 */
public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieHolder> {

    private List<MovieDB> favouriteMovies;
    private Context context;

    public FavouriteMovieAdapter(List<MovieDB> favouriteMovies, Context context) {
        this.favouriteMovies = favouriteMovies;
        this.context = context;
    }

    @Override
    public FavouriteMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.movie_list_view, parent, false);
        return new FavouriteMovieHolder(view, context);
    }

    @Override
    public void onBindViewHolder(FavouriteMovieHolder holder, int position) {
        final MovieDB temp = favouriteMovies.get(position);
        holder.bindMovie(temp);
    }

    public MovieDB getMovie(int position) {
        return favouriteMovies.get(position);
    }

    public void updateUI() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return favouriteMovies.size();
    }
}
