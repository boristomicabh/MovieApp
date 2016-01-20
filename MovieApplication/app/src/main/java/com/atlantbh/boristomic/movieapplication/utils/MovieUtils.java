package com.atlantbh.boristomic.movieapplication.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.activities.MovieActivity;
import com.atlantbh.boristomic.movieapplication.models.Movie;

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
            return getThisMonthDate(movieMonth, movieDay);
        }

        return formattedDate;
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
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

}
