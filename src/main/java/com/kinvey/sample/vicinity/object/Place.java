package com.kinvey.sample.vicinity.object;

import android.util.Log;

import com.google.api.client.util.Key;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.spec.ECField;
import java.util.ArrayList;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class Place implements Serializable {

    @Key
    public String formatted_address;

    @Key
    public String formatted_phone_number;

    @Key
    public Geometry geometry;

    @Key
    public String icon;

    @Key
    public String place_id;

    @Key
    public String international_phone_number;

    @Key
    public String name;

    @Key
    public String rating;

    @Key
    public ArrayList<Review> reviews;

    @Key
    public ArrayList<String> types;

    @Key
    public String vicinity;

    @Key
    public String website;

    @Key
    public String reference;

    public String getReference() {
        if (reference != null) {
            return reference;
        } else {
            return "";
        }
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getFormatted_address() {
        if (formatted_address != null) {
            return formatted_address;
        } else {
            return "";
        }
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        if (formatted_phone_number != null) {
            return formatted_phone_number;
        } else {
            return "";
        }
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public Geometry getGeometry() {
        if (geometry != null) {
            return geometry;
        } else {
            return new Geometry();
        }
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        if (icon != null) {
            return icon;
        } else {
            return "";
        }
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        if (place_id != null) {
            return place_id;
        } else {
            return "";
        }
    }

    public void setId(String id) {
        this.place_id = id;
    }

    public String getInternational_phone_number() {
        if (international_phone_number != null) {
            return international_phone_number;
        } else {
            return "";
        }
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        if (rating != null) {
            return rating;
        } else {
            return "";
        }
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<Review> getReviews() {
        if (reviews != null) {
            return reviews;
        } else {
            return new ArrayList<Review>();
        }
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getTypes() {
        if (types != null) {
            return types;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        if (vicinity != null) {
            return vicinity;
        } else {
            return "";
        }
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        if (website != null) {
            return website;
        } else {
            return "";
        }
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public static Place jsonToPlaceSearch(JSONObject placeObject) {
        try {
            Place result = new Place();

            if (placeObject.has("name") && !placeObject.isNull("name")) {
                result.setName(placeObject.getString("name"));
            }

            if (placeObject.has("rating") && !placeObject.isNull("rating")) {
                result.setRating(placeObject.getString("rating"));
            }

            if (placeObject.has("geometry") && !placeObject.isNull("geometry")) {

                Geometry geometry = new Geometry();

                JSONObject k = placeObject.getJSONObject("geometry");

                if (k.has("location") && !k.isNull("location")) {
                    Location location = new Location();

                    JSONObject j = k.getJSONObject("location");

                    if (j.has("lat") && !j.isNull("lat")) {
                        location.setLat(Double.parseDouble(j.getString("lat")));
                    }
                    if (j.has("lng") && !j.isNull("lng")) {
                        location.setLng(Double.parseDouble(j.getString("lng")));
                    }
                    geometry.setLocation(location);
                }
                result.setGeometry(geometry);
            }

            if (placeObject.has("vicinity") && !placeObject.isNull("vicinity")) {
                result.setVicinity(placeObject.getString("vicinity"));
            }

            if (placeObject.has("place_id") && !placeObject.isNull("place_id")) {
                result.setId(placeObject.getString("place_id"));
            }

            return result;
        } catch (Exception ex) {

            Log.d("Exception in parsing", ex.getMessage());
        }
        return new Place();
    }


    public static Place jsonToPlaceDetails(JSONObject placeObject) {

        try {
            Place result = new Place();

            if (placeObject.has("name") && !placeObject.isNull("name")) {
                result.setName(placeObject.getString("name"));
            }

            if (placeObject.has("rating") && !placeObject.isNull("rating")) {
                result.setRating(placeObject.getString("rating"));
            }

            if (placeObject.has("geometry") && !placeObject.isNull("geometry")) {

                Geometry geometry = new Geometry();

                JSONObject k = placeObject.getJSONObject("geometry");

                if (k.has("location") && !k.isNull("location")) {
                    Location location = new Location();

                    JSONObject j = k.getJSONObject("location");

                    if (j.has("lat") && !j.isNull("lat")) {
                        location.setLat(Double.parseDouble(j.getString("lat")));
                    }
                    if (j.has("lng") && !j.isNull("lng")) {
                        location.setLng(Double.parseDouble(j.getString("lng")));
                    }
                    geometry.setLocation(location);
                }
                result.setGeometry(geometry);
            }

            if (placeObject.has("vicinity") && !placeObject.isNull("vicinity")) {
                result.setVicinity(placeObject.getString("vicinity"));
            }

            if (placeObject.has("website") && !placeObject.isNull("website")) {
                result.setWebsite(placeObject.getString("website"));
            }

            if (placeObject.has("international_phone_number") && !placeObject.isNull("international_phone_number")) {
                result.setInternational_phone_number(placeObject.getString("international_phone_number"));
            }

            if (placeObject.has("place_id") && !placeObject.isNull("place_id")) {
                result.setId(placeObject.getString("place_id"));
            }

            if (placeObject.has("formatted_address") && !placeObject.isNull("formatted_address")) {
                result.setFormatted_address(placeObject.getString("formatted_address"));
            }

            if (placeObject.has("reviews") && !placeObject.isNull("reviews")) {

                JSONArray jsonArray = placeObject.getJSONArray("reviews");
                ArrayList<Review> reviews = new ArrayList<Review>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    Review review = Review.getReviewsFromJSON((JSONObject)jsonArray.get(i));
                    reviews.add(review);
                }

                result.setReviews(reviews);
            }

            return result;
        } catch (Exception ex) {
            Log.d("Exception in parsing", ex.getMessage());
        }
        return new Place();
    }


}
