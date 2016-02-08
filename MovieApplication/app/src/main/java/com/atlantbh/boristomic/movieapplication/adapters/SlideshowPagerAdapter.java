package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.fragments.SlideshowFragment;
import com.atlantbh.boristomic.movieapplication.models.rest.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.rest.Trailer;
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
    List<Object> objects;
    FragmentManager fragmentManager;

    public SlideshowPagerAdapter(Context context, List<Object> objects, FragmentManager fragmentManager) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragmentManager = fragmentManager;
        if (objects.size() > 25) {
            this.objects = objects.subList(0, 25);
        } else {
            this.objects = objects;
        }
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = inflater.inflate(R.layout.backdrop_image_slideshow, container, false);
        final FrameLayout video = (FrameLayout) view.findViewById(R.id.video_trailer);
        final ImageView backdropImage = (ImageView) view.findViewById(R.id.backdrop_image_slideshow);
        final Object object = objects.get(position);

        if (object instanceof Backdrop) {
            video.setVisibility(View.GONE);
            backdropImage.setVisibility(View.VISIBLE);
            final Backdrop backdrop = (Backdrop) object;
            Picasso.with(context).load(MovieUtils.getBackdropURLForGallery(Constants.BACKDROP_SIZE_W780, backdrop)).into(backdropImage);
        } else if (object instanceof Trailer) {
            backdropImage.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            video.requestFocus();
            video.bringToFront();
//            CirclePageIndicator indicator = (CirclePageIndicator) ((Activity) context).findViewById(R.id.pager_indicator);
//            indicator.setVisibility(View.GONE);

            final Trailer trailer = (Trailer) object;
            SlideshowFragment youtube = SlideshowFragment.newInstance(trailer.getKey());
            fragmentManager.beginTransaction().replace(R.id.video_trailer, youtube).commit();
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
