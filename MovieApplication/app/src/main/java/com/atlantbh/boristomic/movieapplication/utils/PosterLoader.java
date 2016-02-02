package com.atlantbh.boristomic.movieapplication.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by boristomic on 02/02/16.
 */
public class PosterLoader implements Target {

    private final String LOG_TAG = PosterLoader.class.getSimpleName();

    private ImageView posterImage;
    private Toolbar toolbar;

    public PosterLoader(ImageView posterImage, Toolbar toolbar) {
        this.posterImage = posterImage;
        this.toolbar = toolbar;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (bitmap != null) {
            posterImage.setImageBitmap(bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette != null) {
                        Palette.Swatch color = palette.getVibrantSwatch();
                        Palette.Swatch color2 = palette.getMutedSwatch();
                        if (color != null) {
                            toolbar.setBackgroundColor(color.getRgb());
                        } else {
                            if (color2 != null) {
                                toolbar.setBackgroundColor(color2.getRgb());
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.e(LOG_TAG, "Failed to load bitmap onBitmapFailed");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.e(LOG_TAG, "Failed to load bitmap onPrepareLoad" );
    }
}
