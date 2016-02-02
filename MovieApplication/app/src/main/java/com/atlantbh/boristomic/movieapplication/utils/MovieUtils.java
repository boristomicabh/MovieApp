package com.atlantbh.boristomic.movieapplication.utils;

import com.atlantbh.boristomic.movieapplication.models.rest.Actor;
import com.atlantbh.boristomic.movieapplication.models.rest.Backdrop;
import com.atlantbh.boristomic.movieapplication.models.rest.Cast;
import com.atlantbh.boristomic.movieapplication.models.rest.Genre;
import com.atlantbh.boristomic.movieapplication.models.rest.Movie;
import com.atlantbh.boristomic.movieapplication.models.rest.Trailer;
import com.atlantbh.boristomic.movieapplication.models.rest.Videos;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by boristomic on 19/01/16.
 */
public class MovieUtils {

    public static String getPosterURL(String size, Movie movie) {
        return Constants.URL_BASE_IMG + size + movie.getPosterPath();
    }

    public static String getBackdropURL(String size, Movie movie) {
        if (movie.getBackdropPath() == null) {
            return null;
        }
        return Constants.URL_BASE_IMG + size + movie.getBackdropPath();
    }

    public static String getCastImageURL(String size, Cast cast) {
        if (cast.getProfilePath() == null) {
            return Constants.URL_BASE_IMG + size + cast.getPosterPath();
        }
        return Constants.URL_BASE_IMG + size + cast.getProfilePath();
    }

    public static String getBackdropURLForGallery(String size, Backdrop backdrop) {
        return Constants.URL_BASE_IMG + size + backdrop.getFilePath();
    }

    public static String getShorterOverview(Movie movie) {
        if (movie.getOverview().length() > 225) {
            return movie.getOverview().substring(0, 220) + "...";
        }
        return movie.getOverview();
    }

    public static String getMovieYear(Movie movie) {
        if (movie.getReleaseDate() == null) {
            return movie.getFirstAirDate().split("-")[0];
        }
        return movie.getReleaseDate().split("-")[0];
    }

    public static String getUpcomingMovieDate(Movie movie) {
        final Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        int currentMonth = calendar.get(Calendar.MONTH);
        int movieMonth = Integer.parseInt(movie.getReleaseDate().split("-")[1]);
        int movieDay = Integer.parseInt(movie.getReleaseDate().split("-")[2]);

        int result = movieMonth - (currentMonth + 1);

        String formattedDate = getNumberFormattedString(result);

        if (formattedDate == null) {
            return getThisMonthDate(currentMonth, movieDay);
        }

        return formattedDate;
    }

    public static String getTitleWithYear(Movie movie, int type) {
        if (type == Constants.TV_SHOWS) {
            return movie.getName() + " (" + getMovieYear(movie) + ")";
        } else if (type == Constants.UPCOMING_MOVIES) {
            return movie.getTitle() + " (" + getUpcomingMovieDate(movie) + ")";
        }
        return movie.getTitle() + " (" + getMovieYear(movie) + ")";
    }

    public static String getMovieTrailer(Videos videos) {
        for (Trailer t : videos.getResults()) {
            if (Constants.YOUTUBE.equals(t.getSite())) {
                return t.getKey();
            }
        }
        return null;
    }

    private static String getThisMonthDate(int month, int day) {
        StringBuilder result = new StringBuilder();
        result.append(getMonthForInt(month));
        result.append(" ");
        result.append(ordinalSufix(day));
        return result.toString();
    }

    private static String getNumberFormattedString(int number) {
        switch (number) {
            case 1:
                return "Next month";
            case 2:
                return "In two months";
            case 3:
                return "In three months";
            case 4:
                return "In four months";
            case 5:
                return "In five months";
            case 6:
                return "In six months";
            case 7:
                return "In seven months";
            case 8:
                return "In eight months";
            case 9:
                return "In nine months";
            case 10:
                return "In ten months";
            case 11:
                return "In eleven months";
            case 12:
                return "In one year";
            default:
                return null;
        }
    }

    private static String ordinalSufix(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }

    private static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static String getDurationAndGenre(Movie movie) {
        return getMovieTime(movie) + " | " + getGenreNames(movie);
    }

    public static float getMovieFloatRating(Movie movie) {
        return (float) movie.getVoteAverage() / 2;
    }

    public static String getMovieStringRating(Movie movie) {
        return String.format("%.1f", movie.getVoteAverage() / 2);
    }

    public static String getGenreNames(Movie movie) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < movie.getGenres().size(); i++) {
            builder.append(movie.getGenres().get(i).getName());
            if (i + 1 != movie.getGenres().size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static String getMovieTime(Movie movie) {
        if (movie.getRuntime() != 0) {
            int hours = movie.getRuntime() / 60;
            int min = movie.getRuntime() - 60 * hours;

            return hours + " h " + min + " m";
        }
        StringBuilder mins = new StringBuilder();
        for (int n : movie.getEpisodeRuntime()) {
            mins.append(n);
            mins.append(" m ");
        }
        return mins.toString();
    }

    public static String getActorBirthData(Actor actor) {
        return "Born " + actor.getBirthday() + " in " + actor.getPlaceOfBirth();
    }


}
