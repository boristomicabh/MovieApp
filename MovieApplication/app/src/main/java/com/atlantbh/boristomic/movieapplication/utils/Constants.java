package com.atlantbh.boristomic.movieapplication.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.atlantbh.boristomic.movieapplication.activities.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by boristomic on 19/01/16.
 */
public class Constants {

    private static final String LOG_TAG = Constants.class.getSimpleName();

    public final static String API_KEY = getStringFromFile("api.key");

    public static final String URL_MOVIE_ID = "/movie/{id}";
    public static final String URL_TV_ID = "/tv/{id}";
    public static final String URL_ACTOR_ID = "/person/{id}";
    public static final String URL_TV = "/tv";
    public static final String URL_SEARCH_MOVIE = "/search/movie";
    public static final String URL_DISCOVER_MOVIE = "/discover/movie";
    public static final String URL_DISCOVER_TV = "/discover/tv";
    public static final String URL_TOP_RATED_MOVIES = "/movie/top_rated";
    public static final String URL_UPCOMING_MOVIES = "/movie/upcoming";
    public static final String URL_TOP_RATED_TV_SHOWS = "/tv/top_rated";
    public static final String URL_MOVIE_CREDITS = "/movie/{id}/credits";
    public static final String URL_TV_CREDITS = "/tv/{id}/credits";
    public static final String URL_MOVIE_VIDEOS = "/movie/{id}/videos";
    public static final String URL_TV_VIDEOS = "/tv/{id}/videos";
    public static final String URL_MOVIE_IMAGES = "/movie/{id}/images";
    public static final String URL_TV_IMAGES = "/tv/{id}/images";
    public static final String URL_ACTOR_TAGGED_IMAGES = "/person/{id}/tagged_images";
    public static final String URL_ACTOR_MOVIE_CREDITS = "/person/{id}/movie_credits";
    public static final String URL_ACTOR_TV_CREDITS = "/person/{id}/tv_credits";

    public static final String URL_BASE = "https://api.themoviedb.org/3";
    public static final String URL_BASE_IMG = "http://image.tmdb.org/t/p/";

    public static final String QUERY_QUERY = "query";
    public static final String QUERY_SORT_BY = "sort_by";
    public static final String QUERY_POPULARITY_DESC = "popularity.desc";
    public static final String QUERY_PRIMARY_RELEASE_YEAR = "primary_release_year";
    public static final String QUERY_API_KEY = "api_key";

    public static final String PATH_ID = "id";

    public static final String POSTER_SIZE_W92 = "w92";
    public static final String POSTER_SIZE_W154 = "w154";
    public static final String POSTER_SIZE_W185 = "w185";
    public static final String POSTER_SIZE_W342 = "w342";
    public static final String POSTER_SIZE_W500 = "w500";

    public static final String BACKDROP_SIZE_W300 = "w300";
    public static final String BACKDROP_SIZE_W500 = "w500";
    public static final String BACKDROP_SIZE_W780 = "w780";
    public static final String BACKDROP_SIZE_W1280 = "w1280";

    public static final String PROFILE_SIZE_W45 = "w45";
    public static final String PROFILE_SIZE_W185 = "w185";
    public static final String PROFILE_SIZE_H632 = "h632";

    public static final int OTHER_LISTS = 1;
    public static final int UPCOMING_MOVIES = 2;
    public static final int TV_SHOWS = 3;
    public static final int MOVIE = 4;
    public static final int IMAGE = 5;
    public static final int CAST = 6;
    public static final int ACTOR_MOVIES = 7;
    public static final int ACTOR_TV_SHOWS = 8;

    public static final int MOST_POPULAR_MOVIES_LIST = 11;
    public static final int TOP_RATED_MOVIES_LIST = 12;
    public static final int UPCOMING_MOVIES_LIST = 13;
    public static final int MOST_POPULAR_TV_SHOWS_LIST = 14;
    public static final int TOP_RATED_TV_SHOWS_LIST = 15;

    public static final String INTENT_KEY = "id";
    public static final String FRAGMENT_KEY = "list type";
    public static final String INTENT_KEY_TYPE_TV_SHOW = "tvShow";
    public static final String INTENT_KEY_TYPE_MOVIE = "movie";

    public static final String YOUTUBE = "YouTube";
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    public static final String MOVIE_REVIEW_URL_BASE = "https://www.themoviedb.org/movie/";
    public static final String MOVIE_REVIEW_URL_EXTRA = "/reviews";
    public static final String TV_SHOW_INFO_URL_BASE = "https://www.themoviedb.org/tv/";

    private static String getStringFromFile(String s) {
        Context context = MainActivity.getContext();
        AssetManager assetManager = context.getAssets();
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = assetManager.open("config.properties");
            properties.load(is);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to load config.properties", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Failed to close input stream", e);
                }
            }
        }
        return properties.getProperty(s);
    }

}
