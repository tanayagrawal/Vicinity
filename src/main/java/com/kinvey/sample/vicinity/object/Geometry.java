package com.kinvey.sample.vicinity.object;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class Geometry implements Serializable {

    @Key
    public Location location;

    public Location getLocation() {
        if(location!=null) {
            return location;
        }else{
            return new Location();
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
