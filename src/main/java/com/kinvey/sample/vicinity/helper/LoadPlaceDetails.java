package com.kinvey.sample.vicinity.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.kinvey.sample.vicinity.object.Place;

import java.util.ArrayList;

/**
 * Created by tanayagrawal on 16/11/15.
 */
public class LoadPlaceDetails extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread Show Progress Dialog
     */
    Context context;
    ProgressDialog pDialog;
    String placeId;
    GooglePlaces googlePlaces;
    Place placeDetails;


    public LoadPlaceDetails(Context context, String placeId) {
        this.context = context;
        this.placeId = placeId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places Details..."));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * getting Places JSON
     */
    protected String doInBackground(String... args) {
        // creating Places class object
        googlePlaces = new GooglePlaces();

        try {

            googlePlaces.getPlaceDetails(placeId);

            Log.d("Near Places", placeDetails.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * and show the data in UI
     * Always use runOnUiThread(new Runnable()) to update UI from background
     * thread, otherwise you will get error
     **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread


    }

}