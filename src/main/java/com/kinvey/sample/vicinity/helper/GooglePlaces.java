package com.kinvey.sample.vicinity.helper;

import android.util.Log;
import com.kinvey.sample.vicinity.object.Place;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Tanay Agrawal on 10/16/2015.
 */
public class GooglePlaces {

    private String API_KEY = "AIzaSyDmAUhEFwiFTp30tazcnfU96idHDYDgWco";

    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String placeType, double radius) {

        String urlString = makeUrl(latitude, longitude, placeType, radius);

        try {
            String json = getJSON(urlString);

            Log.d("JSON String", "" + json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place.jsonToPlaceSearch((JSONObject) array.get(i));
                    Log.v("Google Places ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Log.d("PlaceSearchException", "" + ex.getMessage());
        }
        return new ArrayList<Place>();
    }

    public Place getPlaceDetails(String placeId) {

        String urlString = makeUrl(placeId);
        try {
            String json = getJSON(urlString);
            JSONObject object = new JSONObject(json);

            JSONObject placeDetails = object.getJSONObject("result");

            Place place = Place.jsonToPlaceDetails(placeDetails);
            Log.d("Place Details" , "" + json);
            return place;

        } catch (Exception ex) {
            Log.d("PlaceDetailException", "" + ex.getMessage());
        }
        return new Place();
    }

    private String makeUrl(double latitude, double longitude, String place, double radius) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/search/json?");
        urlString.append("&location=");
        urlString.append(Double.toString(latitude));
        urlString.append(",");
        urlString.append(Double.toString(longitude));
        urlString.append("&radius=" + radius);
        urlString.append("&types=" + place);
        urlString.append("&sensor=false&key=" + API_KEY);

        return urlString.toString();
    }

    private String makeUrl(String placeId) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/details/json?");

        urlString.append("placeid=" + placeId);
        urlString.append("&key=" + API_KEY + "&sensor=false");
        return urlString.toString();
    }

    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}



