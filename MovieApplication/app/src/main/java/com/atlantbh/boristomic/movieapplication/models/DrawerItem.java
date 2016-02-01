package com.atlantbh.boristomic.movieapplication.models;

/**
 * Created by boristomic on 30/01/16.
 */
public class DrawerItem {

    private String title;
    private int icon;

    public DrawerItem() {
    }

    public DrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
