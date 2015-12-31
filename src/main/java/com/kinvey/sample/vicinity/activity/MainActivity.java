
package com.kinvey.sample.vicinity.activity;

import com.google.api.client.http.HttpTransport;
import com.kinvey.android.Client;
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.configuration.BootstrapApplication;
import com.kinvey.sample.vicinity.fragment.FragmentDrawer;
import com.kinvey.sample.vicinity.fragment.MainListFragment;
import com.kinvey.sample.vicinity.fragment.MapsFragment;
import com.kinvey.sample.vicinity.util.GPSTracker;
import com.kinvey.sample.vicinity.util.SetTypeface;
import com.kinvey.sample.vicinity.util.Utils;


import android.graphics.Typeface;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


    private static final Level LOGGING_LEVEL = Level.FINEST;
    private Client kinveyClient;
    public FragmentDrawer drawerFragment;
    public static int globalPosition = 0;
    private Menu menu;

    public static double latitude;
    public static double longitude;
    GPSTracker gpsTracker;

    Fragment fragment = null;
    Fragment mContent;

    @Bind(R.id.tool_bar)
    Toolbar toolbar;
    @Bind(R.id.tv_toolbar)
    TextView textView_toolbar;
    @Bind(R.id.instruction)
    TextView textView_instruction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.getLogger(HttpTransport.class.getName()).setLevel(LOGGING_LEVEL);
        ButterKnife.bind(this);

        BootstrapApplication.initializeStorage(this);

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
        }

        init();
        initializeTypeface();

    }


    public void init() {
//        kinveyClient = BootstrapApplication.getInstance().getKinveyService();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textView_toolbar.setText(getResources().getString(R.string.app_name));
        gpsTracker = new GPSTracker(this);
        getCoordinates();

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
    }

    public void initializeTypeface() {
        SetTypeface typeface = new SetTypeface(this);
        Typeface tf = typeface.getFont("regular");
        textView_toolbar.setTypeface(tf);
        textView_instruction.setTypeface(tf);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fragment != null) {
            getSupportFragmentManager().putFragment(outState, "mContent", fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("")) {

            Utils.setSharedPreferences(MainActivity.this, "viewType", "list");
            menu.findItem(R.id.action_map_view).setIcon(R.drawable.ic_action_map_marker);

        } else if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("list")) {

            menu.findItem(R.id.action_map_view).setIcon(R.drawable.ic_action_map_marker);

        } else if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("map")) {

            menu.findItem(R.id.action_map_view).setIcon(R.drawable.ic_action_list_view);
        }
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

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_map_view) {
            if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("list")) {

                Utils.setSharedPreferences(MainActivity.this, "viewType", "map");
                displayView(globalPosition);
                menu.findItem(R.id.action_map_view).setIcon(R.drawable.ic_action_list_view);

            } else if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("map")) {
                Utils.setSharedPreferences(MainActivity.this, "viewType", "list");
                displayView(globalPosition);
                menu.findItem(R.id.action_map_view).setIcon(R.drawable.ic_action_map_marker);

            }
        } else if (id == R.id.action_savedPlaces) {

            Intent intent = new Intent(MainActivity.this, SavedPlacesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        globalPosition = position;
        textView_instruction.setVisibility(View.GONE);
        displayView(position);
    }

    Bundle bundle = new Bundle();

    private void displayView(int position) {

        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                bundle.putString("value", getResources().getString(R.string.atm_code));
                title = getString(R.string.atm);
                break;
            case 1:
                bundle.putString("value", getResources().getString(R.string.bakery_code));
                title = getString(R.string.bakery);
                break;
            case 2:
                bundle.putString("value", getResources().getString(R.string.bank_code));
                title = getString(R.string.bank);
                break;
            case 3:
                bundle.putString("value", getResources().getString(R.string.bars_code));
                title = getString(R.string.bars);
                break;
            case 4:
                bundle.putString("value", getResources().getString(R.string.bookstore_code));
                title = getString(R.string.bookstore);
                break;
            case 5:
                bundle.putString("value", getResources().getString(R.string.department_code));
                title = getString(R.string.department);
                break;
            case 6:
                bundle.putString("value", getResources().getString(R.string.doctor_code));
                title = getString(R.string.doctor);
                break;
            case 7:
                bundle.putString("value", getResources().getString(R.string.hospital_code));
                title = getString(R.string.hospital);
                break;
            case 8:
                bundle.putString("value", getResources().getString(R.string.hotels_code));
                title = getString(R.string.hotels);
                break;
            case 9:
                bundle.putString("value", getResources().getString(R.string.movie_theatre_code));
                title = getString(R.string.movie_theatre);
                break;
            case 10:
                bundle.putString("value", getResources().getString(R.string.park_code));
                title = getString(R.string.park);
                break;
            case 11:
                bundle.putString("value", getResources().getString(R.string.petrol_code));
                title = getString(R.string.petrol);
                break;
            case 12:
                bundle.putString("value", getResources().getString(R.string.pharmacy_code));
                title = getString(R.string.pharmacy);
                break;
            case 13:
                bundle.putString("value", getResources().getString(R.string.police_code));
                title = getString(R.string.police);
                break;
            case 14:
                bundle.putString("value", getResources().getString(R.string.post_office_code));
                title = getString(R.string.post_office);
                break;
            case 15:
                bundle.putString("value", getResources().getString(R.string.restaurant_code));
                title = getString(R.string.restaurant);
                break;
            case 16:
                bundle.putString("value", getResources().getString(R.string.saloon_code));
                title = getString(R.string.saloon);
                break;
            case 17:
                bundle.putString("value", getResources().getString(R.string.school_code));
                title = getString(R.string.school);
                break;
            case 18:
                bundle.putString("value", getResources().getString(R.string.temple_code));
                title = getString(R.string.temple);
                break;

            default:
                break;
        }
        textView_toolbar.setText(title);
        startFragment();
    }

    public void startFragment() {
        if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("map")) {
            fragment = new MapsFragment();
        } else if (Utils.getSharedPreferences(MainActivity.this, "viewType").equals("list")) {
            fragment = new MainListFragment();
        }
        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    public void getCoordinates() {

        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
}


