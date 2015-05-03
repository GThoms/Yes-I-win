package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.location.Location;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

//Shows your target, allows you to eliminate if in range

public class TargetActivity extends Activity {


    Location currentLocation;
    Location targetLocation;

    //Navigation drawer items
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    ParseUser target;

    Button eliminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        // Navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //Find eliminate button
        eliminate = (Button) findViewById(R.id.eliminate);
        buttonListeners();

        //Set Current Target
        target = (ParseUser) ParseUser.getCurrentUser().get("target");
        currentLocation = (Location) ParseUser.getCurrentUser().get("location");
        targetLocation = (Location) target.get("location");

        //compareDistance();


        /*
        ParseQuery<ParseUser> query = ParseQuery.getQuery("Player");
        query.whereEqualTo("fbId", ParseUser.getCurrentUser().get("target"));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        */
    }

    //Calculate distance between two latitude/longitude points
    public double calculateDistance(double currentLatitude, double currentLongitude, double targetLatitude, double targetLongitude){

        //Calculate distance from latitude/longitude
        //Formula from http://stackoverflow.com/questions/27928/how-do-i-calculate-distance-between-two-latitude-longitude-points
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(targetLatitude-currentLatitude);  // deg2rad below
        double dLon = deg2rad(targetLongitude-currentLongitude);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(currentLatitude)) * Math.cos(deg2rad(targetLatitude)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km

        return d;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    //For navigation drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    //For navigation drawer
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Set up menu
    //Populate Navigation drawer
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        navItems = getResources().getStringArray(R.array.navItems_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        return true;
    }

    //Open settings if pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonListeners() {

        //Shows button depressed/unpressed when clicked
        eliminate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) eliminate.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(32);
                    eliminate.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(20);
                    lp.height = px(80);
                    eliminate.setLayoutParams(lp);
                }
                return false;
            }
        });

        //Eliminate player
        eliminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParsePush push = new ParsePush();
                // push.setChannel(ParseUser.getCurrentUser().get("target").toString());
                push.setMessage("You've been attacked!!");
                push.sendInBackground();
            }
        });
    }

    //Change dp to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }
}
