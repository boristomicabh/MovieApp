package com.atlantbh.boristomic.movieapplication.listeners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atlantbh.boristomic.movieapplication.activities.FavouriteMoviesActivity;
import com.atlantbh.boristomic.movieapplication.activities.MainActivity;

/**
 * Created by boristomic on 01/02/16.
 */
public class DrawerMenuItemClicked implements ListView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private Context context;

    public DrawerMenuItemClicked(DrawerLayout drawerLayout, Context context) {
        this.drawerLayout = drawerLayout;
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawers();
        if (position == 0) {
            if (!context.equals(MainActivity.getContext())) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else if (position == 1) {
            Intent intent = new Intent(context, FavouriteMoviesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (position == 2) {
            // TODO add activity for my ratings
        }
    }
}
