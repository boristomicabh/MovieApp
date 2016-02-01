package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.holders.FavouriteMovieHolder;
import com.atlantbh.boristomic.movieapplication.models.MovieDB;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import java.util.List;

import io.realm.Realm;

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
        final View view = inflater.inflate(R.layout.favourite_movie, parent, false);
        return new FavouriteMovieHolder(view, context);
    }

    @Override
    public void onBindViewHolder(FavouriteMovieHolder holder, int position) {
        final MovieDB temp = favouriteMovies.get(position);
        int type = Constants.MOVIE;
        if (temp.getName() == null) {
            holder.getMovieTitle().setText(temp.getTitle());
        } else {
            type = Constants.TV_SHOWS;
            holder.getMovieTitle().setText(temp.getName());
        }
        holder.getRemoveMovie().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getInstance(context);
                MovieDB.updateMovieFavourite(realm, temp);
                notifyDataSetChanged();
            }
        });

        holder.setIdAndYpe(temp.getId(), type);
    }

    @Override
    public int getItemCount() {
        return favouriteMovies.size();
    }
}
