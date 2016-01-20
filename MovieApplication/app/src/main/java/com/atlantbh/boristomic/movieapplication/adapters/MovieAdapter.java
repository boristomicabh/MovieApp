package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
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
    private int listType;

    public MovieAdapter(List<Movie> movies, int listType) {
        this.movies = movies;
        this.listType = listType;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
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
        ButterKnife.bind(context, view);

        final Movie temp = movies.get(position);

        final TextView mMovieTitle = (TextView) view.findViewById(R.id.list_movie_title);
        if (listType == Constants.TV_SHOWS) {
            mMovieTitle.setText(temp.getName());
        } else {
            mMovieTitle.setText(temp.getTitle());
        }
        final ImageView mMoviePoster = (ImageView) view.findViewById(R.id.list_movie_poster);
        Picasso.with(context).load(MovieUtils.getPosterURL(Constants.POSTER_SIZE_W342, temp)).into(mMoviePoster);
        final TextView mMovieYear = (TextView) view.findViewById(R.id.list_movie_year);
        if (listType == Constants.UPCOMING_MOVIES) {
            mMovieYear.setText(MovieUtils.getUpcomingMovieDate(temp));
        } else {
            mMovieYear.setText(MovieUtils.getMovieYear(temp));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(temp.getId()), Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

}
