package com.jhua.assassin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class RemainingLeaderboard extends Activity {
    ListView winsList;
    ArrayList<String> friends;
    ArrayList<ParseUser> friendObjects;
    ArrayAdapter adapter;
    ArrayList<String> sorta;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remaining_leaderboard);

        context = this.getApplicationContext();

        winsList = (ListView) findViewById(R.id.listView2);

        friends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");

        sorta = new ArrayList<String>();

        for (String S : friends) {
            sorta.add(S);
        }

        sorta.add(ParseUser.getCurrentUser().getUsername());


        friendObjects = new ArrayList<ParseUser>();

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", sorta);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        friendObjects.addAll(list);
                        if (friendObjects != null) {
                            if (!friendObjects.isEmpty()) {
                                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, sorta) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                        if (sorta != null && friendObjects != null) {
                                            text1.setText("Number of Wins: " + friendObjects.get(position).get("wins").toString());
                                            text1.setTextColor(0xff000000);

                                            text1.setTextSize(20);
                                            text2.setText(friendObjects.get(position).getUsername().toString());
                                            text2.setTextColor(0xff000000);
                                           text2.setTextSize(20);
                                        }
                                        return view;

                                    }
                                };


                                winsList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
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


    @Override
    public void onResume() {
        super.onResume();

        sorta.clear();

        context = this.getApplicationContext();

        winsList = (ListView) findViewById(R.id.listView2);

        friends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");

        sorta = new ArrayList<String>();

        for (String S : friends) {
            sorta.add(S);
        }

        sorta.add(ParseUser.getCurrentUser().getUsername());


        friendObjects = new ArrayList<ParseUser>();

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereContainedIn("username", sorta);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        friendObjects.addAll(list);
                        if (friendObjects != null) {
                            if (!friendObjects.isEmpty()) {
                                adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, sorta) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                        TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                        if (sorta != null && friendObjects != null) {
                                            text1.setText("Number of Wins: " + friendObjects.get(position).get("wins").toString());
                                            text1.setTextColor(0xff000000);

                                            text1.setTextSize(20);
                                            text2.setText(friendObjects.get(position).getUsername().toString());
                                            text2.setTextColor(0xff000000);
                                            text2.setTextSize(20);
                                        }
                                        return view;

                                    }
                                };


                                winsList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
