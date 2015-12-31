package com.kinvey.sample.vicinity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.adapter.SavedPlacesListAdapter;
import com.kinvey.sample.vicinity.configuration.BootstrapApplication;
import com.kinvey.sample.vicinity.object.Place;
import com.kinvey.sample.vicinity.util.SetTypeface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedPlacesActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)
    Toolbar toolbar;

    @Bind(R.id.savedPlaces_listView)
    ListView savedPlacesListView;

    @Bind(R.id.tv_toolbar)
    TextView textView_toolbar;

    @Bind(R.id.noResponse_TextView)
    TextView noResponseTextView;

    @Bind(R.id.noResponse_cardView)
    CardView noResponseCardView;

    public static Place selectedPlace;
    ArrayList<Place> savedPlacesArrayList;
    SavedPlacesListAdapter listAdapter;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeTypeface();
        init();

    }

    public void initializeTypeface() {
        SetTypeface typeface = new SetTypeface(this);
        Typeface tf = typeface.getFont("regular");

        textView_toolbar.setTypeface(tf);
        noResponseTextView.setTypeface(tf);


    }

    public void init() {
        textView_toolbar.setText("Saved Places");

        readFileAndPopulateListView();
        clickListener();
    }

    public void clickListener() {
        savedPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                selectedPlace = savedPlacesArrayList.get(position);
                Log.d("TAG", "In Saved Places, ID : " + selectedPlace.getId());

                Intent intent = new Intent(SavedPlacesActivity.this, PlaceDetailsActivity.class);
                intent.putExtra("comingFrom", "savedPlacesListView");
                startActivity(intent);
            }
        });

        savedPlacesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int checkedCount = savedPlacesListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                listAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                toolbar.setVisibility(View.GONE);
                mode.getMenuInflater().inflate(R.menu.menu_saved_places, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_delete:

                        String jsonStringSavedPlaces = BootstrapApplication.readFile("savedPlaces.txt");
                        ArrayList<Place> savedPlacesArrayListFromFile = new ArrayList<Place>();
                        try {

                            if(!jsonStringSavedPlaces.equals("")) {

                                JSONArray jsonArray = new JSONArray(jsonStringSavedPlaces);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //Convert the JSON string to object array
                                    Place place = Place.jsonToPlaceDetails((JSONObject) jsonArray.get(i));
                                    savedPlacesArrayListFromFile.add(place);
                                }
                            }
                        }catch(Exception e){
                                e.printStackTrace();
                            }

                        SparseBooleanArray selected = listAdapter.getSelectedIds();
                        // Captures all selected ids with a loop

                        for (int i = (selected.size() - 1); i >= 0; i--) {

                            if (selected.valueAt(i)) {
                                Place selectedItem = savedPlacesArrayList.get(selected.keyAt(i));
                                // Remove selected items following the ids
                                savedPlacesArrayListFromFile.remove(selected.keyAt(i));
                                listAdapter.remove(selectedItem);
                            }
                        }
                        // Close CAB
                        mode.finish();

                        gson = new Gson();
                        String jsonString = gson.toJson(savedPlacesArrayListFromFile);

                        //Write the JSON string back to the file
                        BootstrapApplication.storeData("savedPlaces.txt", jsonString);
                        readFileAndPopulateListView();
                        return true;
                    default:
                        return false;
                }


            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listAdapter.removeSelection();
                toolbar.setVisibility(View.VISIBLE);

            }
        });
    }

    public void readFileAndPopulateListView() {

        String jsonStringSavedPlaces = BootstrapApplication.readFile("savedPlaces.txt");
        savedPlacesArrayList = new ArrayList<Place>();

        try {

            JSONArray jsonArray = new JSONArray(jsonStringSavedPlaces);

            for (int i = 0; i < jsonArray.length(); i++) {

                //Convert the JSON string to object array
                Place place = Place.jsonToPlaceDetails((JSONObject) jsonArray.get(i));

                savedPlacesArrayList.add(place);
            }
        } catch (Exception e) {
            Log.d("Exception caught", e.getMessage());
        }

        if (savedPlacesArrayList.size() > 0) {

            noResponseCardView.setVisibility(View.GONE);
            noResponseTextView.setVisibility(View.GONE);

            listAdapter = new SavedPlacesListAdapter(this, savedPlacesArrayList);
            savedPlacesListView.setAdapter(listAdapter);
            savedPlacesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        } else {

            noResponseCardView.setVisibility(View.VISIBLE);
            noResponseTextView.setVisibility(View.VISIBLE);
        }
    }
}
