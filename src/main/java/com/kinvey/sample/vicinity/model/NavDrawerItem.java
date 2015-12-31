package com.kinvey.sample.vicinity.model;

/**
 * Created by Tanay Agrawal on 10/12/2015.
 */
public class NavDrawerItem {

    private String title;
    private int image;

    public NavDrawerItem() {

    }

    public NavDrawerItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

