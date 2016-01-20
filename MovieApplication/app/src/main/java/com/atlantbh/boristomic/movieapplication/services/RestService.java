package com.atlantbh.boristomic.movieapplication.services;

import com.atlantbh.boristomic.movieapplication.utils.Constants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by boristomic on 19/01/16.
 */
public class RestService {

    private static MovieAPI movieAPI;

    static {
        setupRestService();
    }

    private RestService() {

    }

    public static MovieAPI get() {
        return movieAPI;
    }

    private static void setupRestService() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Constants.URL_BASE).setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(new Interceptor()).build();
        movieAPI = restAdapter.create(MovieAPI.class);
    }


    private static class Interceptor implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam(Constants.QUERY_API_KEY, Constants.API_KEY);
        }
    }

}
