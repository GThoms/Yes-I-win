package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;

//Shows your target, allows you to eliminate if in range

public class TargetActivity extends Activity {

    //Locations of current user and target
    Location currentLocation;
    Location targetLocation;

    //Game settings
    double attackRadius;

    //Target of the current ParseUser
    ParseUser target;

    //Current Game
    Game game;

    private Drawable rectangle;
    private Drawable circle;
    ImageView back_circle;
    ImageView top_rect;
    ImageView bottom_rect;

    //Navigation drawer items
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    Button eliminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        TextView uname = (TextView) findViewById(R.id.uname_text);
        ImageView profPic = (ImageView) findViewById(R.id.profile_picture);
        back_circle = (ImageView) findViewById(R.id.blue_circle);
        top_rect = (ImageView) findViewById(R.id.rectangle_top);
        bottom_rect = (ImageView) findViewById(R.id.rectangle_bottom);
        eliminate = (Button) findViewById(R.id.eliminate);

        //Store game of the current player
        game = (Game) ParseUser.getCurrentUser().get("game");
        //target = ParseUser.getCurrentUser().get
        if (game == null || target == null) {
            uname.setText("NO TARGET");
            TextView distance = (TextView) findViewById(R.id.dist_text);
            distance.setText("YOU ARE NOT IN A GAME");
            Drawable pic = getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait);
            rectangle = getResources().getDrawable(R.drawable.rectangle_red);
            circle = getResources().getDrawable(R.drawable.circle_red);
            profPic.setImageDrawable(pic);
            top_rect.setImageDrawable(rectangle);
            bottom_rect.setImageDrawable(rectangle);
            back_circle.setImageDrawable(circle);
        } else {
            //Set Current Target
            target = (ParseUser) ParseUser.getCurrentUser().get("target");

            //Store attack radius in km
            attackRadius = game.getAttackRadius() * 0.0009144;

            uname.setText(target.getUsername());
            ParseFile fileObject = (ParseFile) target.get("pic");
            fileObject.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView profPic = (ImageView) findViewById(R.id.profile_picture);
                        profPic.setImageBitmap(bmp);
                    }
                }
            });

            refreshButtonListeners();
        }

        //Shows button depressed/unpressed when clicked
        eliminate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) eliminate.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(70);
                    lp.topMargin = px(40);
                    eliminate.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(30);
                    lp.height = px(80);
                    eliminate.setLayoutParams(lp);
                }
                return false;
            }
        });

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
    }


    public double compareDistance() {
        double currentLongitude = currentLocation.getLongitude();
        double currentLatitude = currentLocation.getLatitude();
        double targetLongitude = targetLocation.getLongitude();
        double targetLatitude = targetLocation.getLatitude();

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

    //Refreshes locations stored in activity from locations of ParseUsers
    public void refreshLocations() {
        currentLocation = (Location) ParseUser.getCurrentUser().get("location");
        targetLocation = (Location) target.get("location");

        //Within distance
        double distance = compareDistance();
        if (distance <= attackRadius) {
            //red
            rectangle = getResources().getDrawable(R.drawable.rectangle_red);
            circle = getResources().getDrawable(R.drawable.circle_red);
            top_rect.setImageDrawable(rectangle);
            bottom_rect.setImageDrawable(rectangle);
            back_circle.setImageDrawable(circle);

            eliminateButtonListeners();

        } else if (distance > (attackRadius) && distance <= (attackRadius * 4)){
            //purple
            rectangle = getResources().getDrawable(R.drawable.rectangle_purple);
            circle = getResources().getDrawable(R.drawable.circle_purple);
            top_rect.setImageDrawable(rectangle);
            bottom_rect.setImageDrawable(rectangle);
            back_circle.setImageDrawable(circle);

        } else {
            //blue
            rectangle = getResources().getDrawable(R.drawable.rectangle_blue);
            circle = getResources().getDrawable(R.drawable.circle_blue);
            top_rect.setImageDrawable(rectangle);
            bottom_rect.setImageDrawable(rectangle);
            back_circle.setImageDrawable(circle);
        }
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

        if (id == R.id.action_refresh) {
            refreshLocations();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void eliminateButtonListeners() {

        eliminate.setText("ELIMINATE");

        //Eliminate player
        eliminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParsePush push = new ParsePush();
                //ParseUser myTarget = ParseUser.getCurrentUser().get("target");
                // push.setChannel(ParseUser.getCurrentUser().get("target").toString());
                push.setMessage("You've been attacked!!");
                // Code to reassign target
                /*
                String name = ParseUser.getCurrentUser().getUsername().toString();
                ParseUser targetsTarget = target.get("target");  // get their target, your target now
                ParseUser.getCurrentUser().put("target", targetsTarget);  // puts ParseUser object in target field for this ParseUser
                
                // remove target here
                
                */
                push.sendInBackground();
            }
        });
    }

    public void refreshButtonListeners() {

        eliminate.setText("REFRESH");

        eliminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLocations();
            }
        });

    }

    //Change dp to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }
}
