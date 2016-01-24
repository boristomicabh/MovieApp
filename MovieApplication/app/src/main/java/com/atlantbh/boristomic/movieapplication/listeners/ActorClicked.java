package com.atlantbh.boristomic.movieapplication.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.atlantbh.boristomic.movieapplication.activities.ActorActivity;
import com.atlantbh.boristomic.movieapplication.models.Cast;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

/**
 * Created by boristomic on 24/01/16.
 */
public class ActorClicked implements View.OnClickListener {

    private Cast cast;
    private Context context;

    public ActorClicked(Cast cast, Context context) {
        this.cast = cast;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(Constants.INTENT_KEY, cast.getId());
        context.startActivity(intent);
    }
}
