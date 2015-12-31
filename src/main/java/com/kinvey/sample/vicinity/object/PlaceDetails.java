package com.kinvey.sample.vicinity.object;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class PlaceDetails implements Serializable {
    @Key
    public String status;

    @Key
    public Place result;

    public String getStatus() {
        if(status!=null) {
            return status;
        }else{
            return "";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Place getResult() {
        if(result!=null) {
            return result;
        }else{
            return new Place();
        }
    }

    public void setResult(Place result) {
        this.result = result;
    }
}
