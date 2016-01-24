package com.atlantbh.boristomic.movieapplication.services;

import com.atlantbh.boristomic.movieapplication.models.Actor;
import com.atlantbh.boristomic.movieapplication.models.ActorImages;
import com.atlantbh.boristomic.movieapplication.models.Cast;
import com.atlantbh.boristomic.movieapplication.models.Credits;
import com.atlantbh.boristomic.movieapplication.models.Images;
import com.atlantbh.boristomic.movieapplication.models.Movie;
import com.atlantbh.boristomic.movieapplication.models.MoviesResponse;
import com.atlantbh.boristomic.movieapplication.models.Videos;
import com.atlantbh.boristomic.movieapplication.utils.Constants;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by boristomic on 19/01/16.
 */
public interface MovieAPI {

    /**
     * Used to get most popular movies, by default it's sorted by popularity descending
     *
     * @param sortBy   <code>String</code> type value by which list of movies will be sorted, by default it's popularity in descending order
     * @param callback <code>MoviesResponse</code> type value that is received from API, includes list of movies
     */
    @GET(Constants.URL_DISCOVER_MOVIE)
    void listPopularMovies(@Query(Constants.QUERY_SORT_BY) String sortBy, Callback<MoviesResponse> callback);

    /**
     * Used to get best movies in given year, by default it's sorted by average vote in descending order
     *
     * @param year     <code>int</code> type value of year
     * @param sortBy   <code>String</code> type value by which list of movies will be sorted, by default it's average vote in descending order
     * @param callback <code>MoviesResponse</code> type value that is received from API, includes list of movies
     */
    @GET(Constants.URL_DISCOVER_MOVIE)
    void listBestMoviesByYear(@Query(Constants.QUERY_PRIMARY_RELEASE_YEAR) int year, @Query(Constants.QUERY_SORT_BY) String sortBy, Callback<MoviesResponse> callback);

    @GET(Constants.URL_TOP_RATED_MOVIES)
    void listTopRatedMovies(Callback<MoviesResponse> callback);

    @GET(Constants.URL_UPCOMING_MOVIES)
    void listUpcomingMovies(Callback<MoviesResponse> callback);

    @GET(Constants.URL_TOP_RATED_TV_SHOWS)
    void listTopRatedTvShows(Callback<MoviesResponse> callback);

    @GET(Constants.URL_DISCOVER_TV)
    void listPopularTvShows(@Query(Constants.QUERY_SORT_BY) String sortBy, Callback<MoviesResponse> callback);

    @GET(Constants.URL_MOVIE_ID)
    void findSingleMovie(@Path("id") long id, Callback<Movie> callback);

    @GET(Constants.URL_TV_ID)
    void findSingleTvShow(@Path("id") long id, Callback<Movie> callback);

    @GET(Constants.URL_MOVIE_ID + "/credits")
    void findMovieCast(@Path("id") long id, Callback<Credits> callback);

    @GET(Constants.URL_TV_ID + "/credits")
    void findTvShowCast(@Path("id") long id, Callback<Credits> callback);

    @GET(Constants.URL_MOVIE_ID + "/videos")
    void findMovieVideo(@Path("id") long id, Callback<Videos> callback);

    @GET(Constants.URL_TV_ID + "/videos")
    void findTvShowVideo(@Path("id") long id, Callback<Videos> callback);

    @GET(Constants.URL_MOVIE_ID + "/images")
    void findMovieBackdrops(@Path("id") long id, Callback<Images> callback);

    @GET(Constants.URL_TV_ID + "/images")
    void findTvShowBackdrops(@Path("id") long id, Callback<Images> callback);

    @GET(Constants.URL_ACTOR_ID)
    void findActor(@Path("id") long id, Callback<Actor> callback);

    @GET(Constants.URL_ACTOR_ID + "/tagged_images")
    void findActorImages(@Path("id") long id, Callback<ActorImages> callback);

    @GET(Constants.URL_ACTOR_ID + "/movie_credits")
    void findActorMovies(@Path("id") long id, Callback<Credits> callback);

    @GET(Constants.URL_ACTOR_ID + "/tv_credits")
    void findActorTVShows(@Path("id") long id, Callback<Credits> callback);




}
