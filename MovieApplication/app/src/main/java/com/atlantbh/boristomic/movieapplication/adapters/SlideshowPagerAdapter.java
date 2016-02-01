package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.models.rest.Backdrop;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by boristomic on 01/02/16.
 */
public class SlideshowPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<Backdrop> backdrops;

    public SlideshowPagerAdapter(Context context, List<Backdrop> backdrops) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.backdrops = backdrops;
    }

    @Override
    public int getCount() {
        return backdrops.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = inflater.inflate(R.layout.backdrop_image_slideshow, container, false);
        final ImageView backdropImage = (ImageView) view.findViewById(R.id.backdrop_image_slideshow);
        final Backdrop backdrop = backdrops.get(position);
        Picasso.with(context).load(MovieUtils.getBackdropURLForGallery(Constants.BACKDROP_SIZE_W780, backdrop)).into(backdropImage);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
