package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.listeners.MovieClicked;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;

import java.util.List;

/**
 * Created by boristomic on 25/01/16.
 */
public class MovieSearchAdapter extends BaseAdapter {

    private List<Movie> movies;

    public MovieSearchAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View initConvertView(final Context context, final View convertView, final ViewGroup viewGroup) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.search_movie, viewGroup, false);
            return view;
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final View view = initConvertView(context, convertView, parent);
        final TextView movieTitle = (TextView) view.findViewById(R.id.search_movie_title);
        final TextView movieYear = (TextView) view.findViewById(R.id.search_movie_year);
        final Movie movie = movies.get(position);
        movieTitle.setText(movie.getOriginalTitle());
        movieYear.setText(MovieUtils.getMovieYear(movie));
        view.setOnClickListener(new MovieClicked(movie, context, Constants.MOVIE));
        return view;
    }
}
