package com.kinvey.sample.vicinity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.activity.MainActivity;
import com.kinvey.sample.vicinity.activity.PlaceDetailsActivity;
import com.kinvey.sample.vicinity.adapter.ListViewAdapter;
import com.kinvey.sample.vicinity.helper.GooglePlaces;
import com.kinvey.sample.vicinity.object.Place;
import com.kinvey.sample.vicinity.object.PlacesList;
import com.kinvey.sample.vicinity.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static String types;
    public static PlacesList placesList;
    public static HashMap<Marker, Place> hashMarkerPlace;
    public static Marker finalMarker;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        types = getArguments().getString("value");
        placesList = new PlacesList();
        context = getContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_maps, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return layout;

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        int radius = Utils.getSharedPreferenceInteger(getContext(), "radius") * 1000;


        try {
            if (Utils.hasConnection()) {
                LoadPlaces loadPlaces = new LoadPlaces(context, types, radius, MainActivity.latitude, MainActivity.longitude);
                loadPlaces.execute();
            } else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("Place List Exception", "" + e.getMessage());
        }
        mMap.setMyLocationEnabled(true);
        Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(MainActivity.latitude, MainActivity.longitude)).radius(radius)
                .strokeColor(getResources().getColor(R.color.colorPrimary)));
        circle.setVisible(true);

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

    class LoadPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        Context context;
        ProgressDialog pDialog;
        String types;
        double radius, latitude, longitude;
        GooglePlaces googlePlaces;
        ArrayList<Place> nearPlaces;


        public LoadPlaces(Context context, String types, int radius, double latitude, double longitude) {
            this.context = context;
            this.radius = (double) radius;
            this.latitude = latitude;
            this.longitude = longitude;
            this.types = types;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         */
        @Override
        protected ArrayList<Place> doInBackground(Void... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {

                nearPlaces = googlePlaces.findPlaces(latitude, longitude, types, radius);

                Log.d("Near Places", nearPlaces.toString());
                return nearPlaces;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<Place>();
        }


        @Override
        protected void onPostExecute(final ArrayList<Place> result) {
            // dismiss the dialog after getting all products
            super.onPostExecute(result);
            double lat, lng;

            pDialog.dismiss();
            ArrayList<Marker> markers = new ArrayList<Marker>();
            hashMarkerPlace = new HashMap<Marker,Place>();

            for (int i = 0; i < result.size(); i++) {

                lat = result.get(i).getGeometry().getLocation().getLat();
                lng = result.get(i).getGeometry().getLocation().getLng();

                Location location1 = new Location("Point 1");
                Location location2 = new Location("Point 2");

                location1.setLatitude(latitude);
                location1.setLongitude(longitude);

                location2.setLatitude(lat);
                location2.setLongitude(lng);

                float distance = location1.distanceTo(location2);
                float distanceInKm = distance / 1000;
                DecimalFormat f = new DecimalFormat("##.0");

                markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title(result.get(i).getName()).snippet("" + f.format(distanceInKm) + " KM")));

            }

            for (int i = 0; i < markers.size(); i++) {
                hashMarkerPlace.put(markers.get(i), result.get(i));
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    finalMarker = marker;
                    Intent intent = new Intent(getActivity(), PlaceDetailsActivity.class);
                    intent.putExtra("comingFrom", "mapView");
                    startActivity(intent);
                }
            });

        }

    }
}

