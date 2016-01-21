package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.models.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.Cast;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by boristomic on 21/01/16.
 */
public class ImageAdapter extends BaseAdapter {

    private List<Backdrop> backdrops;
    private int listType;

    public ImageAdapter(Images images, int listType) {
        this.backdrops = images.getBackdrops();
        this.listType = listType;
    }

    @Override
    public int getCount() {
        return backdrops.size();
    }

    @Override
    public Object getItem(int position) {
        return backdrops.get(position);
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
        //ButterKnife.bind(context, view);

        final Backdrop temp = backdrops.get(position);

//        final TextView mMovieTitle = (TextView) view.findViewById(R.id.list_movie_title);
//        if (listType == Constants.TV_SHOWS) {
//            mMovieTitle.setText(temp.getName());
//        } else {
//            mMovieTitle.setText(temp.getName());
//        }
        final ImageView mMoviePoster = (ImageView) view.findViewById(R.id.list_movie_poster);
        Picasso.with(context).load(MovieUtils.getBackdropURLForGallery(Constants.BACKDROP_SIZE_W300, temp)).into(mMoviePoster);
//        final TextView mMovieYear = (TextView) view.findViewById(R.id.list_movie_year);
//        if (listType == Constants.UPCOMING_MOVIES) {
//            mMovieYear.setText(MovieUtils.getUpcomingMovieDate(temp));
//        } else {
//            mMovieYear.setText(MovieUtils.getMovieYear(temp));
//        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, temp.getVoteCount(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MovieActivity.class);
//                Bundle bundle = new Bundle();
//                intent.putExtra(Constants.INTENT_KEY, temp.getId());
//                if (listType == Constants.TV_SHOWS) {
//                    intent.putExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, Constants.TV_SHOWS);
//                }
//                context.startActivity(intent);
            }
        });
        return view;
    }
}
