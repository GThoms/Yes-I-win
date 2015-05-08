package com.jhua.assassin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;


public class FriendList extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    Button addFriends;
    ListView friendList;

    boolean found;
    EditText friendName;
    Button addFriend;
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


        addFriends = (Button) findViewById(R.id.friendButton);
        final ListView friendList = (ListView) findViewById(R.id.friendList);

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layinf = LayoutInflater.from(FriendList.this);
                View promptsView = layinf.inflate(R.layout.add_friend, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendList.this);
                builder.setView(promptsView);

                builder.setTitle(R.string.title_dialog_add_friend);
                //builder.setIcon(R.drawable.whiteplus);

                friendName = (EditText) promptsView.findViewById(R.id.editFriend);

                builder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFriendtoParse();
                        ArrayList<String> friends = (ArrayList<String>)ParseUser.getCurrentUser().get("friends");
                        System.out.println(friends);

                        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, friends);

                        if (friends != null) {
                            friendList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
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

        friendList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }

        });
    }

    private void addFriendtoParse() {
        player = friendName.getText().toString();

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
                            ParseUser.getCurrentUser().addUnique("friends", player);
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    }
                }
            }
        });
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
}
