package com.jhua.assassin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.facebook.*;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	protected static final int LOGIN_OK = 1;

    //GamesList Adapters and objects
	ListView gamesListView;
	GameListAdapter gameListAdapter;
	List<Map<String,?>> currentGames;
	List<Map<String,?>> pendingGames;
	List<Map<String,?>> completedGames;

	public final static String ITEM_TITLE = "title";  
    public final static String ITEM_CAPTION = "caption";
    
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] navItems;
	private ActionBarDrawerToggle mDrawerToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        //initialize parse for this application using our specific hash keys
		Parse.initialize(this, "hxFZwmGDuKwt2BXEoyGTcPuPuFc8IJkx3eQD2DV4", "o3P37KBeAVP4970XyU0AXgrserg7qT6EEmI4J47r");
		ParseInstallation.getCurrentInstallation().saveInBackground();

        //Setting up games list
		gamesListView = (ListView) findViewById(R.id.gamesList);
		this.setUpGamesList();

		//Navigation Drawer stuff
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Easier way to do drawer opening/closing
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

<<<<<<< HEAD
		// if not logged in
=======


		// test
		//Intent test = new Intent(MainActivity.this, FacebookKeyHash.class);
		//startActivity(test);

        // if not logged in, login through Facebook
>>>>>>> origin/master
		if (ParseUser.getCurrentUser() == null) {
			Intent login = new Intent(MainActivity.this, FacebookActivity.class);
			startActivity(login);
		}

		// check google play services
		if (!isGooglePlayServicesAvailable()) {
			finish();
		}
	}


    //For navigation drawer
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    //For naviagation drawer
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    //Items that link (string - string) value pairs in a map
    //Used for game list items
    //Title is linked to key ITEM_TITLE
    //Caption is linked to key ITEM_CAPTION
	public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }


    //Create menu
    //Populate navigation drawer
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


    //For navigation drawer
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	    }
		
		int id = item.getItemId();


        //Open settings
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		Intent service = new Intent(MainActivity.this, LocationService.class);
		stopService(service);
	}
	
	/**
	 * Sets up the section headers and populates each section from database
	 */
	@SuppressLint("ResourceAsColor")
	private void setUpGamesList() {

        //Linked list of current games
        currentGames = new LinkedList<Map<String,?>>();
		currentGames.add(createItem("Example 1", "blah"));
        currentGames.add(createItem("Example 2", "blah"));
        currentGames.add(createItem("Example 3", "blah"));


        //Linked list of pending games
        pendingGames = new LinkedList<Map<String,?>>();
        pendingGames.add(createItem("Example 1", "blah"));
        pendingGames.add(createItem("Example 2", "blah"));
        pendingGames.add(createItem("Example 3", "blah"));


        //Linked list of completed games
        completedGames = new LinkedList<Map<String,?>>();
        completedGames.add(createItem("My Game", "Mccoy East"));
        completedGames.add(createItem("His Game", "blah"));
        completedGames.add(createItem("Her Game", "blah"));

        //Make a new adapter for the game list
		GameListAdapter gameListAdapter = new GameListAdapter(this);
		
		// Current Games, set up new adapter from linked list
		SimpleAdapter currentGamesAdapter = new SimpleAdapter(this, currentGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add current game adapter to game list adapter
        gameListAdapter.addSection("In Progress", currentGamesAdapter);
		
		// Pending Games, set up adapter from linked list
		SimpleAdapter pendingGamesAdapter = new SimpleAdapter(this, pendingGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add pending game adapter to game list adapter
        gameListAdapter.addSection("Pending", pendingGamesAdapter);
		
	    // Completed Games, set up adapter from linked list
	    SimpleAdapter completedGamesAdapter = new SimpleAdapter(this, completedGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add completed game adapter to game list adapter
        gameListAdapter.addSection("Complete", completedGamesAdapter);

        //Add adapter to game list, actually populates game list here
		gamesListView.setAdapter(gameListAdapter);
	}


    //Allows us to use Dialogs in our activity
	private void showDialog(char type) {
		// TODO : stopService() when user leaves game, startService() when user enters game
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

		View promptView;
		if (type == 'p') {
			promptView = layoutInflater.inflate(R.layout.pending_dialog, null);
		}
		else if (type == 'a') {
			promptView = layoutInflater.inflate(R.layout.leave_dialog, null);
		}
		else {
			promptView = layoutInflater.inflate(R.layout.delete_dialog, null);
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setView(promptView);

		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}


    //Sees if we have google play services available
	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

}
