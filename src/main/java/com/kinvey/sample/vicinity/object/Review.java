package com.kinvey.sample.vicinity.object;

import com.google.api.client.util.Key;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tanay Agrawal on 10/15/2015.
 */
public class Review implements Serializable {

    @Key
    public String author_name;

    @Key
    public String language;

    @Key
    public String rating;

    @Key
    public String text;

    public String getAuthor_name() {
        if (author_name != null) {
            return author_name;
        } else {
            return "";
        }
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getText() {
        if (text != null) {
            return text;
        } else {
            return "";
        }
    }

    public void setText(String text) {
        this.text = text;
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

    public String getLanguage() {
        if (language != null) {
            return language;
        } else {
            return "";
        }
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public static Review getReviewsFromJSON(JSONObject reviewObject){

        Review review = new Review();
        try{

            if(reviewObject.has("author_name") && !reviewObject.isNull("author_name")){
                review.setAuthor_name(reviewObject.getString("author_name"));
            }

            if(reviewObject.has("text") && !reviewObject.isNull("text")){
                review.setText(reviewObject.getString("text"));
            }
            if(reviewObject.has("rating") && !reviewObject.isNull("rating")){
                review.setRating(reviewObject.getString("rating"));
            }

            return review;
        }catch (Exception e){

        }
        return new Review();
    }
}
