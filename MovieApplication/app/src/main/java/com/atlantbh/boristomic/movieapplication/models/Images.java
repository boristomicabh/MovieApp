package com.atlantbh.boristomic.movieapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boristomic on 21/01/16.
 */
public class Images {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = new ArrayList<Backdrop>();
    @SerializedName("profiles")
    @Expose
    private List<Backdrop> profiles = new ArrayList<Backdrop>();


    /**
     *
     * @return
     * The id
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The backdrops
     */
    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    /**
     *
     * @param backdrops
     * The backdrops
     */
    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Backdrop> getProfiles() {
        return profiles;
    }
}
