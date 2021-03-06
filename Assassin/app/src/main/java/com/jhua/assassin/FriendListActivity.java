package com.jhua.assassin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FriendListActivity extends Activity {

    //Navigation Drawer Stuff
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    //XML view for list of friends
    ListView friendList;

    //ParseUser's list of friends
    ArrayList<String> friends;

    //Adapter for friend list
    ArrayAdapter adapter;

    //XML views
    EditText friendName;
    Button addFriends;

    //Player you are trying to add
    String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

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

        //Save XML views
        addFriends = (Button) findViewById(R.id.friendButton);
        final ListView friendList = (ListView) findViewById(R.id.friendList);

        //Get friend list from current user
        friends = (ArrayList<String>)ParseUser.getCurrentUser().get("friends");

        //Make a new adapter for friend list
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, android.R.id.text1);

        //If user has friends, set adapter to friend list view

        if (friends == null) {
            adapter.add("You have no friends...");
        } else if (!friends.isEmpty()) {
            adapter.addAll(friends);
        } else {
            adapter.add("You have no friends...");
        }

        friendList.setAdapter(adapter);

        //When friend item is clicked
        friendList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                new AlertDialog.Builder(FriendListActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remove Friend")
                        .setMessage("Do you wish to remove this user from your friends list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //remove friend
                                        Log.d("yes button", "yes was clicked");
                                        String name = (String) adapter.getItem(position);
                                        Log.d("name", name);
                                        ParseUser.getCurrentUser().removeAll("friends", Arrays.asList(name));


                                        ParseUser.getCurrentUser().saveInBackground();

                                        adapter.remove(name);

                                        if (adapter.isEmpty()) {
                                            adapter.add("You have no friends...");
                                        }

                                        adapter.notifyDataSetChanged();
                                        friendList.invalidateViews();
                                    }
                                }).setNegativeButton("No", null).show();
            }

        });
        buttonListeners();
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layinf = LayoutInflater.from(FriendListActivity.this);

                View promptsView = layinf.inflate(R.layout.add_friend, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(FriendListActivity.this);

                builder.setView(promptsView);

                builder.setTitle(R.string.title_dialog_add_friend);
                //builder.setIcon(R.drawable.whiteplus);

                friendName = (EditText) promptsView.findViewById(R.id.editFriend);

                builder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addFriendtoParse();
                        adapter.notifyDataSetChanged();
                        friendList.invalidateViews();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void addFriendtoParse() {

        //Get player to add from XML view
        player = friendName.getText().toString();

        //Query list of friends, see if above player exists in database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", player);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {

                    if (list.isEmpty()) {

                        Toast.makeText(getApplicationContext(),
                                "That User does not exist!", Toast.LENGTH_LONG)
                                .show();
                    } else {

                        if (player.equals(ParseUser.getCurrentUser().getUsername())) {
                            Toast.makeText(getApplicationContext(),
                                    "You can't add yourself as a friend! Go find new ones!", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Adding Friend...", Toast.LENGTH_LONG)
                                    .show();
                            ParseUser.getCurrentUser().addUnique("friends", player);
                            ParseUser.getCurrentUser().saveInBackground();

                            if (adapter.getPosition(player) < 0) {
                                if (adapter.getItem(0).equals("You have no friends...")) {
                                    adapter.remove("You have no friends...");
                                }
                                adapter.add(player);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "You are already friends with " + player,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
    }

    //Navigation Drawer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    //Navigation Drawer
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Navigation Drawer
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

    //Navigation Drawer
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

    private void buttonListeners() {
        //Listener for add friend button
        //Makes a dialog box that lets you add friends
        addFriends.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) addFriends.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(60);
                    lp.topMargin = px(30);
                    addFriends.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(20);
                    lp.height = px(70);
                    addFriends.setLayoutParams(lp);
                }
                return false;
            }
        });
    }

    //Turns dps to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }
}
