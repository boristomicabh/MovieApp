package com.atlantbh.boristomic.movieapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boristomic on 22/01/16.
 */
public class Actor {

    @SerializedName("adult")
    @Expose
    private boolean adult;
    @SerializedName("also_known_as")
    @Expose
    private List<String> alsoKnownAs = new ArrayList<String>();
    @SerializedName("biography")
    @Expose
    private String biography;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("deathday")
    @Expose
    private String deathday;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("place_of_birth")
    @Expose
    private String placeOfBirth;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**
     *
     * @return
     * The adult
     */
    public boolean getAdult() {
        return adult;
    }

    /**
     *
     * @param adult
     * The adult
     */
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    /**
     *
     * @return
     * The alsoKnownAs
     */
    public List<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    /**
     *
     * @param alsoKnownAs
     * The also_known_as
     */
    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.alsoKnownAs = alsoKnownAs;
    }

    /**
     *
     * @return
     * The biography
     */
    public String getBiography() {
        return biography;
    }

    /**
     *
     * @param biography
     * The biography
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     *
     * @return
     * The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     *
     * @param birthday
     * The birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     *
     * @return
     * The deathday
     */
    public String getDeathday() {
        return deathday;
    }

    /**
     *
     * @param deathday
     * The deathday
     */
    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    /**
     *
     * @return
     * The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     *
     * @param homepage
     * The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

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
     * The imdbId
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     *
     * @param imdbId
     * The imdb_id
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The placeOfBirth
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     *
     * @param placeOfBirth
     * The place_of_birth
     */
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    /**
     *
     * @return
     * The popularity
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     *
     * @param profilePath
     * The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
