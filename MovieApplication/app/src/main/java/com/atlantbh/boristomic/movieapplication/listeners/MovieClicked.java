//package com.atlantbh.boristomic.movieapplication.listeners;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
//import com.atlantbh.boristomic.movieapplication.models.Movie;
//import com.atlantbh.boristomic.movieapplication.utils.Constants;
//
///**
// * Created by boristomic on 24/01/16.
// */
//public class MovieClicked implements View.OnClickListener {
//
//    private Movie movie;
//    private Context context;
//    private int listType;
//
//    public MovieClicked(Movie movie, Context context, int listType) {
//        this.movie = movie;
//        this.context = context;
//        this.listType = listType;
//    }
//
//    /**
//     * Movie activity is started and movie id is passed as parameter or
//     * tv show is passed as parameter if tv show is selected
//     *
//     * @param v <code>View</code> with movie or tv show image and title
//     */
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(context, MovieActivity.class);
//        Bundle bundle = new Bundle();
//        intent.putExtra(Constants.INTENT_KEY, movie.getId());
//        if (listType == Constants.TV_SHOWS) {
//            intent.putExtra(Constants.INTENT_KEY_TYPE_TV_SHOW, Constants.TV_SHOWS);
//        }
//        context.startActivity(intent);
//    }
//}
