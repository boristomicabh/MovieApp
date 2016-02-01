package com.atlantbh.boristomic.movieapplication.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boristomic on 22/01/16.
 */
public class ActorImages {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Backdrop> results = new ArrayList<Backdrop>();
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    public ActorImages() {

    }

    public List<Backdrop> getResults() {
        return results;
    }
}
