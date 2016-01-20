package com.atlantbh.boristomic.movieapplication.utils;

/**
 * Created by boristomic on 19/01/16.
 */
public class Constants {

    public static final String URL_MOVIE_ID = "/movie/{id}";
    public static final String URL_TV = "/tv";
    public static final String URL_DISCOVER_MOVIE = "/discover/movie";
    public static final String URL_DISCOVER_TV = "/discover/tv";
    public static final String URL_TOP_RATED_MOVIES = "/movie/top_rated";
    public static final String URL_UPCOMING_MOVIES = "/movie/upcoming";
    public static final String URL_TOP_RATED_TV_SHOWS = "/tv/top_rated";

    public static final String URL_BASE = "https://api.themoviedb.org/3";
    public static final String URL_BASE_IMG = "http://image.tmdb.org/t/p/";

    public static final String QUERY_SORT_BY = "sort_by";
    public static final String QUERY_POPULARITY_DESC = "popularity.desc";
    public static final String QUERY_PRIMARY_RELEASE_YEAR = "primary_release_year";
    public static final String QUERY_API_KEY = "api_key";

    public static final String POSTER_SIZE_W92 = "w92";
    public static final String POSTER_SIZE_W154 = "w154";
    public static final String POSTER_SIZE_W185 = "w185";
    public static final String POSTER_SIZE_W342 = "w342";
    public static final String POSTER_SIZE_W500 = "w500";

    public static final String BACKDROP_SIZE_W300 = "w300";
    public static final String BACKDROP_SIZE_W780 = "w780";
    public static final String BACKDROP_SIZE_W1280 = "w1280";

    public static final int OTHER_LISTS = 1;
    public static final int UPCOMING_MOVIES = 2;
    public static final int TV_SHOWS = 3;

    public static final String INTENT_KEY = "id";

    // TODO move to property file
    public static final String API_KEY = "34aa7e1baaee7e047801a1a8454587b8";


}
