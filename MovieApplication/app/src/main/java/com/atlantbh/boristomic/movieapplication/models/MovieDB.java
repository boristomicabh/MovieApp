package com.atlantbh.boristomic.movieapplication.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by boristomic on 26/01/16.
 */
public class MovieDB extends RealmObject {

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
}
