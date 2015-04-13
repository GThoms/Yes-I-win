package com.example.assassins;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {I'm g'

	ListView gamesListView;
	com.example.assassins.GameListAdapter gameListAdapter;
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
				
		gamesListView = (ListView) findViewById(R.id.gamesList);
		this.setUpGamesList();
		
		//Navigation Drawer stuff
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
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	public Map<String,?> createItem(String title, String caption) {  
        Map<String,String> item = new HashMap<String,String>();  
        item.put(ITEM_TITLE, title);  
        item.put(ITEM_CAPTION, caption);  
        return item;  
    }  

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
	
	/**
	 * Sets up the section headers and populates each section from database
	 */
	@SuppressLint("ResourceAsColor")
	private void setUpGamesList() {
		currentGames = new LinkedList<Map<String,?>>();
		currentGames.add(createItem("Example 1", "blah"));
        currentGames.add(createItem("Example 2", "blah"));
        currentGames.add(createItem("Example 3", "blah"));
        
        pendingGames = new LinkedList<Map<String,?>>();
        pendingGames.add(createItem("Example 1", "blah"));
        pendingGames.add(createItem("Example 2", "blah"));
        pendingGames.add(createItem("Example 3", "blah"));
        
        completedGames = new LinkedList<Map<String,?>>();
        completedGames.add(createItem("Example 1", "blah"));
        completedGames.add(createItem("Example 2", "blah"));
        completedGames.add(createItem("Example 3", "blah"));
		
		GameListAdapter gameListAdapter = new GameListAdapter(this);
		
		// Current Games
		SimpleAdapter currentGamesAdapter = new SimpleAdapter(this, currentGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });
		gameListAdapter.addSection("In Progress", currentGamesAdapter);
		
		// Pending Games
		SimpleAdapter pendingGamesAdapter = new SimpleAdapter(this, pendingGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });
	    gameListAdapter.addSection("Pending", pendingGamesAdapter);
		
	    // Completed Games
	    SimpleAdapter completedGamesAdapter = new SimpleAdapter(this, completedGames, R.layout.game_list_adapter,
	            new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.gameName, R.id.description });
	    gameListAdapter.addSection("Complete", completedGamesAdapter);

		gamesListView.setAdapter(gameListAdapter);
	}

}
