package com.kinvey.sample.vicinity.object;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class PlacesList implements Serializable {

    @Key
    public String status;

    @Key
    public ArrayList<Place> results;

    public ArrayList<Place> getResults() {
        if (results != null) {
            return results;
        } else {
            return new ArrayList<Place>();
        }
    }

    public void setResults(ArrayList<Place> results) {
        this.results = results;
    }

    public String getStatus() {
        if (status != null) {
            return status;
        } else {
            return "";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
