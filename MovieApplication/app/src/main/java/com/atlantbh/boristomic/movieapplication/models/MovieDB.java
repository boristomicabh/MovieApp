package com.atlantbh.boristomic.movieapplication.models;

import android.content.Context;
import android.util.Log;

import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.utils.MovieUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
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
    private String overview;
    private String releaseDate;
    private String runtime;
    private String voteAverage;
    private float vote;
    private String genres;
    private int voteCount;
    private String posterPath;
    private float myRating;

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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public float getVote() {
        return vote;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
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
     * Updates movie users personal rating.
     *
     * @param realm   <code>Realm</code> type object initialized in specific activity
     * @param movieDB <code>MovieDB</code> type object to be updated
     * @param rating  <code>int</code> type value of personal rating
     */
    public static void updateMovieRating(Realm realm, MovieDB movieDB, int rating) {
        realm.beginTransaction();
        movieDB.setMyRating(rating);
        realm.commitTransaction();
    }

    /**
     * Saves new movie to database
     *
     * @param realm <code>Realm</code> type object initialized in specific activity
     * @param movie <code>Movie</code> type object, response gotten from API
     * @return <code>boolean</code> type true if movie is saved, false if not
     */
    public static boolean saveNewMovie(Realm realm, Movie movie) {
        try {
            realm.beginTransaction();
            MovieDB movieToSave = realm.createObject(MovieDB.class);
            movieToSave.setId(movie.getId());
            movieToSave.setTitle(movie.getTitle());
            movieToSave.setName(movie.getName());
            movieToSave.setIsFavourite(true);
            movieToSave.setRuntime(MovieUtils.getMovieTime(movie));
            movieToSave.setGenres(MovieUtils.getGenreNames(movie));
            movieToSave.setReleaseDate(MovieUtils.getMovieYear(movie));
            movieToSave.setOverview(movie.getOverview());
            movieToSave.setVoteAverage(MovieUtils.getMovieStringRating(movie));
            movieToSave.setVote(MovieUtils.getMovieFloatRating(movie));
            movieToSave.setVoteCount(movie.getVoteCount());
            movieToSave.setPosterPath(getPosterPathForRealm(movie.getPosterPath()));
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to save movie", e);
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * @param realm  <code>Realm</code> type object initialized in specific activity
     * @param movie  <code>Movie</code> type object, response gotten from API
     * @param rating <code>int</code> type value of personal rating
     * @return <code>boolean</code> type true if movie is saved, false if not
     */
    public static boolean SaveNewMoveOnRating(Realm realm, Movie movie, int rating) {
        try {
            realm.beginTransaction();
            MovieDB movieToSave = realm.createObject(MovieDB.class);
            movieToSave.setId(movie.getId());
            movieToSave.setTitle(movie.getTitle());
            movieToSave.setName(movie.getName());
            movieToSave.setMyRating(rating);
            movieToSave.setRuntime(MovieUtils.getMovieTime(movie));
            movieToSave.setGenres(MovieUtils.getGenreNames(movie));
            movieToSave.setReleaseDate(MovieUtils.getMovieYear(movie));
            movieToSave.setOverview(movie.getOverview());
            movieToSave.setVoteAverage(MovieUtils.getMovieStringRating(movie));
            movieToSave.setVote(MovieUtils.getMovieFloatRating(movie));
            movieToSave.setVoteCount(movie.getVoteCount());
            movieToSave.setPosterPath(getPosterPathForRealm(movie.getPosterPath()));
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to save movie", e);
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * Retrieves list of movies from database that are favourite to user,
     * sorted by user's rating in descending order.
     *
     * @param context <code>Context</code> type object from witch Realm database is being called
     * @return <code>List</code> of MovieDB type objects
     */
    public static List<MovieDB> findAllFavouriteMovies(Context context) {
        List<MovieDB> list = Realm.getInstance(context).where(MovieDB.class).equalTo("isFavourite", true).findAllSorted("myRating", Sort.DESCENDING);
        return list;
    }

    /**
     * Used to get movie poster path in database, cuts / character from first position in string
     *
     * @param poster <code>String</code> type value of poster path
     * @return <code>String</code> type value of poster path to be saved
     */
    private static String getPosterPathForRealm(String poster) {
        if (poster != null) {
            return poster.substring(1, poster.length());
        }
        return null;
    }
}
