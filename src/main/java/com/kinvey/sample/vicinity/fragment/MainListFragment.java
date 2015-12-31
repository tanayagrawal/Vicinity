package com.kinvey.sample.vicinity.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.activity.MainActivity;
import com.kinvey.sample.vicinity.activity.PlaceDetailsActivity;
import com.kinvey.sample.vicinity.adapter.ListViewAdapter;
import com.kinvey.sample.vicinity.helper.GooglePlaces;
import com.kinvey.sample.vicinity.object.Place;
import com.kinvey.sample.vicinity.util.SetTypeface;
import com.kinvey.sample.vicinity.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainListFragment extends Fragment {

    TextView noResponseTextView;
    CardView noResponseCardView;

    public static String types;
    public static Place finalResult;
    Context context;
    int radius;


    ListView listView;
    ListViewAdapter lvAdapter;

    public MainListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        types = getArguments().getString("value");
        radius = Utils.getSharedPreferenceInteger(getContext(), "radius") * 1000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_main_list, container, false);
        listView = (ListView) layout.findViewById(R.id.listView);
        noResponseTextView = (TextView) layout.findViewById(R.id.noResponse_TextView);
        noResponseCardView = (CardView) layout.findViewById(R.id.noResponse_cardView);
        initializeTypeface();


        if (Utils.hasConnection()) {
            LoadPlaces loadPlaces = new LoadPlaces(context, types, radius, MainActivity.latitude, MainActivity.longitude);
            loadPlaces.execute();
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return layout;

    }

    public void initializeTypeface(){
        SetTypeface typeface = new SetTypeface(context);
        Typeface tf = typeface.getFont("regular");
        noResponseTextView.setTypeface(tf);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            ArrayList<String> placeName = new ArrayList<String>();
            ArrayList<String> placeDistance = new ArrayList<String>();
            ArrayList<String> placeVicinity = new ArrayList<String>();

            if (result.size() > 0) {
                noResponseTextView.setVisibility(View.GONE);
                noResponseCardView.setVisibility(View.GONE);

                for (int i = 0; i < result.size(); i++) {
                    placeName.add(result.get(i).getName());

                    Location location1 = new Location("Point 1");
                    Location location2 = new Location("Point 2");

                    location1.setLatitude(latitude);
                    location1.setLongitude(longitude);

                    location2.setLatitude(result.get(i).getGeometry().getLocation().getLat());
                    location2.setLongitude(result.get(i).getGeometry().getLocation().getLng());

                    float distance = location1.distanceTo(location2);
                    float distanceInKm = distance / 1000;
                    DecimalFormat f = new DecimalFormat("##.0");
                    placeDistance.add("" + f.format(distanceInKm));
                    placeVicinity.add(result.get(i).getVicinity());

                }
                lvAdapter = new ListViewAdapter(context, placeName, placeDistance, placeVicinity);
                listView.setAdapter(lvAdapter);

                pDialog.dismiss();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        finalResult = result.get(position);
                        Intent intent = new Intent(getActivity(), PlaceDetailsActivity.class);
                        intent.putExtra("comingFrom", "fragmentListView");
                        startActivity(intent);
                    }
                });
            }

        }

    }

}


