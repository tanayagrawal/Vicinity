package com.kinvey.sample.vicinity.object;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class Location implements Serializable {

    @Key
    public double lat;

    @Key
    public double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
