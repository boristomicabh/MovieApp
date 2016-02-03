package com.atlantbh.boristomic.movieapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by boristomic on 02/02/16.
 */
public class Connection {

    public static boolean isConnected(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
