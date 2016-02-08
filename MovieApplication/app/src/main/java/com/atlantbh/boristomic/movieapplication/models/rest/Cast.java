package com.atlantbh.boristomic.movieapplication.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by boristomic on 21/01/16.
 */
public class Cast {


    @SerializedName("cast_id")
    @Expose
    private int castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private int order;
    @SerializedName("profile_path")
    @Expose
    private Object profilePath;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    /**
     * @return The castId
     */
    public int getCastId() {
        return castId;
    }

    /**
     * @param castId The cast_id
     */
    public void setCastId(int castId) {
        this.castId = castId;
    }

    /**
     * @return The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * @param character The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * @return The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     * @param creditId The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    /**
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order The order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return The profilePath
     */
    public Object getProfilePath() {
        return profilePath;
    }

    /**
     * @param profilePath The profile_path
     */
    public void setProfilePath(Object profilePath) {
        this.profilePath = profilePath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @param posterPath The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalName() {
        return originalName;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "castId=" + castId +
                ", character='" + character + '\'' +
                ", creditId='" + creditId + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", profilePath=" + profilePath +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
