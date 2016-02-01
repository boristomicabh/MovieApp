package com.atlantbh.boristomic.movieapplication.models;

import android.content.Context;
import android.util.Log;

import com.atlantbh.boristomic.movieapplication.models.rest.Movie;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by boristomic on 26/01/16.
 */
public class MovieDB extends RealmObject {

    private static final String LOG_TAG = MovieDB.class.getSimpleName();

    @PrimaryKey
    private long id;
    private String title;
    private String name;
    private boolean isFavourite = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    /**
     * Finds movie in database for given movie id.
     *
     * @param realm <code>Realm</code> type object initialized in specific activity
     * @param id    <code>long</code> type value of movie id
     * @return <code>MovieDB</code> object if found, null if not
     */
    public static MovieDB findMovieById(Realm realm, long id) {
        return realm.where(MovieDB.class).equalTo("id", id).findFirst();
    }

    /**
     * Updates favourite status of movie in database
     *
     * @param realm   <code>Realm</code> type object initialized in specific activity
     * @param movieDB <code>MovieDB</code> type object to be updated
     * @return <code>boolean</code> type value true if movie is set as favourite, false if not
     */
    public static boolean updateMovieFavourite(Realm realm, MovieDB movieDB) {
        boolean result = false;
        realm.beginTransaction();
        if (movieDB.isFavourite()) {
            movieDB.setIsFavourite(false);
        } else {
            movieDB.setIsFavourite(true);
            result = true;
        }
        realm.commitTransaction();
        return result;
    }

    /**
     * Saves new movie to database
     *
     * @param realm <code>Realm</code> type object initialized in specific activity
     * @param movie <code>Movie</code> type object, response gotten from API
     * @return <code>boolean</code> type true if movie is savd, false if not
     */
    public static boolean saveNewMovie(Realm realm, Movie movie) {
        try {
            realm.beginTransaction();
            MovieDB movieToSave = realm.createObject(MovieDB.class);
            movieToSave.setId(movie.getId());
            movieToSave.setTitle(movie.getTitle());
            movieToSave.setName(movie.getName());
            movieToSave.setIsFavourite(true);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to save movie", e);
            realm.cancelTransaction();
            return false;
        }
    }

    public static List<MovieDB> findAllFavouriteMovies(Context context) {
        List<MovieDB> list = Realm.getInstance(context).where(MovieDB.class).equalTo("isFavourite", true).findAll();
        return list;
    }
}
