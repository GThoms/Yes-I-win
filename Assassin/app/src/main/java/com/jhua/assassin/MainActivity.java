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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.facebook.*;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;


//Main activity of the app
public class MainActivity extends Activity {

    //GamesList Adapters and objects
	ListView gamesListView;
	GameListAdapter gameListAdapter;
	List<Map<String,?>> currentGames;
	List<Map<String,?>> pendingGames;
	List<Map<String,?>> completedGames;

	public final static String ITEM_TITLE = "title";  
    public final static String ITEM_CAPTION = "caption";

    //Current User
    ParseUser user;

    //Navigation drawer stuff
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] navItems;
	private ActionBarDrawerToggle mDrawerToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = ParseUser.getCurrentUser();

		if (user == null) {
			Intent login = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(login);
		} else {
			//Setting up games list
			gamesListView = (ListView) findViewById(R.id.gamesList);
			this.setUpGamesList();

			test();

			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String jsonData = extras.getString("com.parse.Data");
				JSONObject json = null;
				String u_name = "";
				try {
					json = new JSONObject(jsonData);
					u_name = json.get("id").toString(); // username
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d("JSON", jsonData);
				Log.d("Username", u_name);
			}
		}

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
		
		// check google play services
		if (!isGooglePlayServicesAvailable()) {
			finish();
		}
	}

	protected void test() {

		TextView t = (TextView) findViewById(R.id.textView13);
		t.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Channels", "subscribed to " + ParseInstallation.getCurrentInstallation().getList("channels").toString());

				JSONObject data = null;
				try {
					data = new JSONObject("{\"alert\": \"You've been invited to play Assassins!\"}");
					data.put("id", user.getUsername());
					Log.d("JSON", "new JSON object created: " + data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// test (ignore for now)
				ParsePush push = new ParsePush();
				push.setChannel(user.getUsername());
				push.sendDataInBackground(data, ParseInstallation.getQuery(), new SendCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							Log.d("Push", "sent push!");
						} else {
							Log.d("PUSH ERROR", e.toString());
						}
					}
				});
			}
		});
	}

	protected void onResume(Bundle savedInstanceState) {
		super.onResume();

        for (int i = 0; i < 30; i++) {
            this.setUpGamesList();
        }
		// Logs install and app activate App Event
		AppEventsLogger.activateApp(this);
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

        //TextView loadText = (TextView) findViewById(R.id.loadingText);
        //loadText.setText("Loading...");

        //Linked list of current games
        currentGames = new LinkedList<Map<String,?>>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
        query.whereEqualTo("status", "current");
        query.whereEqualTo("players", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gameList, ParseException e) {
                if (e == null) {
                    for ( ParseObject G : gameList ){
                        currentGames.add(createItem(((Game) G).getGameName(), "Players: " + G.get("players").toString()));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        //Linked list of pending games
        //pendingGames = new LinkedList<Map<String,?>>();

        /*query = ParseQuery.getQuery("Game");
        query.whereEqualTo("status", "pending");
        query.whereEqualTo("players", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gameList, ParseException e) {
                if (e == null) {
                    for ( ParseObject G : gameList ){
                        pendingGames.add(createItem(((Game) G).getGameName(), "Players: " + G.get("players").toString()));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });*/


        //Linked list of completed games
        completedGames = new LinkedList<Map<String,?>>();

        query = ParseQuery.getQuery("Game");
        query.whereEqualTo("status", "completed");
        query.whereEqualTo("players", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gameList, ParseException e) {
                if (e == null) {
                    for ( ParseObject G : gameList ){
                        completedGames.add(createItem(((Game) G).getGameName(), "Players: " + G.get("players").toString()));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        //Make a new adapter for the game list
		GameListAdapter gameListAdapter = new GameListAdapter(this);
		
		// Current Games, set up new adapter from linked list
		SimpleAdapter currentGamesAdapter = new SimpleAdapter(this, currentGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add current game adapter to game list adapter
        gameListAdapter.addSection("In Progress", currentGamesAdapter);
		
		// Pending Games, set up adapter from linked list
		//SimpleAdapter pendingGamesAdapter = new SimpleAdapter(this, pendingGames, R.layout.game_list_adapter,
	            //new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add pending game adapter to game list adapter
        //gameListAdapter.addSection("Pending", pendingGamesAdapter);
		
	    // Completed Games, set up adapter from linked list
	    SimpleAdapter completedGamesAdapter = new SimpleAdapter(this, completedGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });

        //Add completed game adapter to game list adapter
        gameListAdapter.addSection("Complete", completedGamesAdapter);

        //Add adapter to game list, actually populates game list here
		gamesListView.setAdapter(gameListAdapter);


        gameListAdapter.notifyDataSetChanged();
        gamesListView.invalidateViews();

        //Remove loading text
        //loadText.setText("");
	}

    //Allows us to use Dialogs in our activity
	private void showDialog(char type) {
		// TODO : stopService() when user leaves game, startService() when user enters game
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		String negative = "Cancel";
		String positive = "Join";

		View promptView;
		if (type == 'p') {
			promptView = layoutInflater.inflate(R.layout.pending_dialog, null);
			negative = "Reject";
			positive = "Join";
		}
		else if (type == 'a') {
			promptView = layoutInflater.inflate(R.layout.leave_dialog, null);
			negative = "Cancel";
			positive = "Leave";
		}
        else if (type == 's') {
            promptView = layoutInflater.inflate(R.layout.start_dialog, null);
			negative = "Cancel";
			positive = "Start";
		}
        else {
			promptView = layoutInflater.inflate(R.layout.delete_dialog, null);
			negative = "Cancel";
			positive = "Delete";
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setView(promptView);

		// setup a dialog window
		alertDialogBuilder.setCancelable(false)
				.setPositiveButton(positive, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				})
				.setNegativeButton(negative,
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

}
