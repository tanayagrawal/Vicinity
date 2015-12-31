package com.kinvey.sample.vicinity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kinvey.android.Client;
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.configuration.BootstrapApplication;
import com.kinvey.sample.vicinity.fragment.MainListFragment;
import com.kinvey.sample.vicinity.fragment.MapsFragment;
import com.kinvey.sample.vicinity.helper.GooglePlaces;
import com.kinvey.sample.vicinity.object.Place;
import com.kinvey.sample.vicinity.object.Review;
import com.kinvey.sample.vicinity.util.SetTypeface;
import com.kinvey.sample.vicinity.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar)
    TextView textView_toolbar;

    @Bind(R.id.navigate_button)
    Button navigateButton;

    @Bind(R.id.tv_website)
    TextView tv_website;

    @Bind(R.id.tv_phone)
    TextView tv_phone;

    @Bind(R.id.tv_address)
    TextView tv_address;

    @Bind(R.id.tv_rating)
    TextView tv_rating;

    @Bind(R.id.tv_reviews)
    TextView tv_reviews;

    public static GoogleMap mMap;
    public static Place result;
    public static HashMap<Marker, Place> markerPlaceHashMap = new HashMap<Marker, Place>();
    public static Marker markerFromMapsFragment;
    public static ArrayList<Review> userReviewsWithText = new ArrayList<Review>();
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
//        kinveyClient = BootstrapApplication.getInstance().getKinveyService();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        initializeTypeface();
        init();
    }

    public void initializeTypeface() {
        SetTypeface typeface = new SetTypeface(this);
        Typeface tf = typeface.getFont("regular");
        textView_toolbar.setTypeface(tf);
        tv_website.setTypeface(tf);
        tv_phone.setTypeface(tf);
        tv_address.setTypeface(tf);
        tv_rating.setTypeface(tf);
        tv_reviews.setTypeface(tf);
        navigateButton.setTypeface(tf);

    }

    public void init() {

        Intent intent = getIntent();
        String comingFrom = intent.getStringExtra("comingFrom");

        String placeId = "";
        String nameOfPlace = "";

        if (comingFrom.equals("fragmentListView")) {

            placeId = MainListFragment.finalResult.getId();
            nameOfPlace = MainListFragment.finalResult.getName();

        } else if (comingFrom.equals("mapView")) {

            markerPlaceHashMap = MapsFragment.hashMarkerPlace;
            markerFromMapsFragment = MapsFragment.finalMarker;

            placeId = markerPlaceHashMap.get(markerFromMapsFragment).getId();
            nameOfPlace = markerPlaceHashMap.get(markerFromMapsFragment).getName();

        }else if(comingFrom.equals("savedPlacesListView")){

            placeId = SavedPlacesActivity.selectedPlace.getId();
            Log.d("TAG", "Place ID : " + placeId );
            nameOfPlace = SavedPlacesActivity.selectedPlace.getName();
            Log.d("TAG", "Place Name : " + nameOfPlace );

        }


        textView_toolbar.setText(nameOfPlace);

        try {
            if (Utils.hasConnection()) {
                new LoadPlaceDetails(this, placeId).execute();
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(PlaceDetailsActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_save) {

            saveLocation();

        }else if(id == R.id.action_savedPlaces){

            Intent intent = new Intent(PlaceDetailsActivity.this, SavedPlacesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        int radius = Utils.getSharedPreferenceInteger(this, "radius") * 1000;

        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(MainActivity.latitude, MainActivity.longitude)).radius(radius)
                .strokeColor(getResources().getColor(R.color.colorPrimary)));
        circle.setVisible(false);


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                circle.getCenter(), getZoomLevel(circle)));
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 1;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    public void saveLocation() {

        if (result != null) {

            //read data from the existing file if it exists

            Toast.makeText(this,"Saving Location...",Toast.LENGTH_SHORT).show();

            String jsonStringSavedPlaces = BootstrapApplication.readFile("savedPlaces.txt");
            ArrayList<Place> savedPlacesArrayList = new ArrayList<Place>();
            try {

                if(!jsonStringSavedPlaces.equals("")) {

                    JSONArray jsonArray = new JSONArray(jsonStringSavedPlaces);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        //Convert the JSON string to object array
                        Place place = Place.jsonToPlaceDetails((JSONObject) jsonArray.get(i));
                        savedPlacesArrayList.add(place);
                    }
                }
                //Add the new object to the top of the object array
                savedPlacesArrayList.add(0, result);

                //Convert the object array back to JSON string
                gson = new Gson();
                String jsonString = gson.toJson(savedPlacesArrayList);

                //Write the JSON string back to the file
                BootstrapApplication.storeData("savedPlaces.txt", jsonString);

                Toast.makeText(this, "Location saved" , Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

                e.printStackTrace();
            }

        } else {

            Toast.makeText(this, "Sorry, The location could not be saved.", Toast.LENGTH_SHORT).show();
        }


    }


    class LoadPlaceDetails extends AsyncTask<Void, Void, Place> {

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
        protected Place doInBackground(Void... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {

                placeDetails = googlePlaces.getPlaceDetails(placeId);

                Log.d("Near Places", "" + placeDetails.toString());
                return placeDetails;
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
        protected void onPostExecute(final Place place) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            result = place;
            final double latitude = place.getGeometry().getLocation().getLat();
            final double longitude = place.getGeometry().getLocation().getLng();

            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
            mMap.addMarker(marker);

            navigateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + latitude + "," + longitude));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            if (!place.getWebsite().isEmpty()) {
                tv_website.setText(place.getWebsite());
            }

            if (!place.getInternational_phone_number().isEmpty()) {
                tv_phone.setText(place.getInternational_phone_number());
            }

            if (!place.getFormatted_address().isEmpty()) {
                tv_address.setText(place.getFormatted_address());
            }

            if (!place.getRating().isEmpty()) {
                tv_rating.setText("" + place.getRating());
            }
            userReviewsWithText.clear();
            for (int i = 0; i < place.getReviews().size(); i++) {

                if (!place.getReviews().get(i).getText().isEmpty()) {
                    userReviewsWithText.add(place.getReviews().get(i));
                }
            }
            if (!userReviewsWithText.isEmpty()) {
                tv_reviews.setText("Tap to view user reviews");
                tv_reviews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PlaceDetailsActivity.this, ReviewsOverlayActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}


