package com.atlantbh.boristomic.movieapplication.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.ActorActivity;
import com.atlantbh.boristomic.movieapplication.models.rest.Cast;
import com.atlantbh.boristomic.movieapplication.utils.Constants;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boristomic on 03/02/16.
 */
public class HorizontalListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.actor_image_list)
    protected ImageView actorImage;
    @Bind(R.id.actor_name_list)
    protected TextView actorName;

    private Context context;
    private long castId;
    private int type;

    public HorizontalListHolder(View itemView, Context context, int type) {
        super(itemView);
        this.context = context;
        this.type = type;
        ButterKnife.bind(this, itemView);
        if (this.type == Constants.CAST) {
            itemView.setOnClickListener(this);
        }
    }

    public void bindCast(Cast cast, int type) {
        this.castId = cast.getId();
        if (type == Constants.MOVIE || type == Constants.TV_SHOWS) {
            bindMovieOrTVShow(cast, type);
        } else {
            if (cast.getProfilePath() == null) {
                Picasso.with(context).load(R.drawable.profile_default).into(actorImage);
            } else {
                Picasso.with(context).load(MovieUtils.getCastImageURL(Constants.PROFILE_SIZE_W185, cast)).into(actorImage);
            }
            actorName.setText(cast.getName());
        }
    }

    public void bindMovieOrTVShow(Cast cast, int type) {
        if (type == Constants.MOVIE) {
            actorName.setText(cast.getOriginalTitle());
        } else {
            actorName.setText(cast.getName());
        }
        if (cast.getPosterPath() == null) {
            Picasso.with(context).load(R.drawable.poster_default).into(actorImage);
        } else {
            Picasso.with(context).load(MovieUtils.getCastImageURL(Constants.PROFILE_SIZE_W185, cast)).into(actorImage);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(Constants.INTENT_KEY, castId);
        context.startActivity(intent);
    }
}
