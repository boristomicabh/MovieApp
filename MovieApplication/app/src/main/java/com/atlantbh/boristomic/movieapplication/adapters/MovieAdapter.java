package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.ActorActivity;
import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.listeners.ActorClicked;
import com.atlantbh.boristomic.movieapplication.listeners.MovieClicked;
import com.atlantbh.boristomic.movieapplication.models.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.Cast;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by boristomic on 19/01/16.
 */
public class MovieAdapter extends BaseAdapter {

    private List<Movie> movies;
    private List<Backdrop> backdrops;
    private List<Cast> cast;
    private int listType;

    public MovieAdapter(List<Movie> movies, Images images, Credits credits, int listType) {
        if (movies != null) {
            if (movies.size() > 10) {
                this.movies = movies.subList(0, 10);
            } else {
                this.movies = movies;
            }
        }
        if (images != null) {
            if (images.getBackdrops() == null) {
                if (images.getProfiles().size() > 10) {
                    this.backdrops = images.getProfiles().subList(0, 10);
                } else {
                    this.backdrops = images.getProfiles();
                }
            } else {
                if (images.getBackdrops().size() > 10) {
                    this.backdrops = images.getBackdrops().subList(0, 10);
                } else {
                    this.backdrops = images.getBackdrops();
                }
            }
        }
        if (credits != null) {
            if (credits.getCast().size() > 8) {
                this.cast = credits.getCast().subList(0, 8);
            } else {
                this.cast = credits.getCast();
            }
        }
        this.listType = listType;
    }

    @Override
    public int getCount() {
        if (movies != null) {
            return movies.size();
        } else if (backdrops != null) {
            return backdrops.size();
        }
        return cast.size();
    }

    @Override
    public Object getItem(int position) {
        if (movies != null) {
            return movies.get(position);
        } else if (backdrops != null) {
            return backdrops.get(position);
        }
        return cast.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private View getView(final Context context, final View convertView, final ViewGroup viewGroup) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.movie_list_view, viewGroup, false);
            return view;
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        final View view = getView(context, convertView, parent);
        final TextView mMovieTitle = (TextView) view.findViewById(R.id.list_movie_title);
        final ImageView mMoviePoster = (ImageView) view.findViewById(R.id.list_movie_poster);
        final TextView mMovieYear = (TextView) view.findViewById(R.id.list_movie_year);

        // TV Shows and movies, show on Feed screen
        if (Constants.OTHER_LISTS == listType || Constants.UPCOMING_MOVIES == listType || Constants.TV_SHOWS == listType) {
            final Movie temp = movies.get(position);
            if (listType == Constants.TV_SHOWS) {
                mMovieTitle.setText(temp.getName());
            } else {
                mMovieTitle.setText(temp.getTitle());
            }
            Picasso.with(context).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, temp)).resize(342, 513).into(mMoviePoster);
            if (listType == Constants.UPCOMING_MOVIES) {
                mMovieYear.setText(MovieUtils.getUpcomingMovieDate(temp));
            } else {
                mMovieYear.setText(MovieUtils.getMovieYear(temp));
            }
            view.setOnClickListener(new MovieClicked(temp, context, listType));
            return view;
        }

        // Show images in list view
        if (Constants.IMAGE == listType) {
            final Backdrop temp = backdrops.get(position);
            if(temp.getWidth() > temp.getHeight()) {
                Picasso.with(context).load(MovieUtils.getBackdropURLForGallery(Constants.BACKDROP_SIZE_W300, temp)).into(mMoviePoster);
            }
            // TODO add pager for gallery
            return view;
        }


        // Only cast left to populate
        final Cast temp = cast.get(position);

        // Show list of actor movies and tv shows
        if (Constants.ACTOR_MOVIES == listType || Constants.ACTOR_TV_SHOWS == listType) {
            Picasso.with(context).load(MovieUtils.getCastImageURL(Constants.POSTER_SIZE_W185, temp)).resize(185, 262).into(mMoviePoster);
            if (Constants.ACTOR_MOVIES == listType) {
                mMovieTitle.setText(temp.getOriginalTitle());
            } else {
                mMovieTitle.setText(temp.getName());
            }
            return view;
        }

        // Show list of actors in a movie
        mMovieTitle.setText(temp.getName());
        Picasso.with(context).load(MovieUtils.getCastImageURL(Constants.PROFILE_SIZE_W185, temp)).resize(185, 262).into(mMoviePoster);
        view.setOnClickListener(new ActorClicked(temp, context));
        return view;
    }

}
