package com.jhua.assassin;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class EliminateLeaderboard extends Activity {

    ListView eliminateList;
    ArrayList<String> friends;
    ArrayList<ParseUser> friendObjects;
    ArrayAdapter adapter;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminate_leaderboard);

        context = this.getApplicationContext();

        eliminateList = (ListView) findViewById(R.id.listView3);

        friends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");

        friendObjects = new ArrayList<ParseUser>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("username", friends);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    friendObjects.addAll(list);
                    friendObjects.add(ParseUser.getCurrentUser());
                    if (friendObjects != null) {
                        if (!friendObjects.isEmpty()) {
                            adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, friends) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                    if (friends != null && friendObjects != null) {
                                        text1.setText("Players Eliminated: " + friendObjects.get(position).get("eliminations").toString());
                                        text1.setTextColor(0xff000000);

                                        text1.setTextSize(20);
                                        text2.setText(friendObjects.get(position).getUsername().toString());
                                        text2.setTextColor(0xff000000);
                                        text2.setTextSize(20);
                                    }
                                    return view;

                                }
                            };


                            eliminateList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }




        });




    }

    @Override
    public void onResume() {
        super.onResume();

        friends = null;

        context = this.getApplicationContext();

        eliminateList = (ListView) findViewById(R.id.listView3);

        friends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");

        friendObjects = new ArrayList<ParseUser>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("username", friends);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    friendObjects.addAll(list);
                    friendObjects.add(ParseUser.getCurrentUser());
                    if (friendObjects != null) {
                        if (!friendObjects.isEmpty()) {
                            adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_2, android.R.id.text1, friends) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                                    if (friends != null && friendObjects != null) {
                                        text1.setText("Players Eliminated: " + friendObjects.get(position).get("eliminations").toString());
                                        text1.setTextColor(0xff000000);

                                        text1.setTextSize(20);
                                        text2.setText(friendObjects.get(position).getUsername().toString());
                                        text2.setTextColor(0xff000000);
                                        text2.setTextSize(20);
                                    }
                                    return view;

                                }
                            };


                            eliminateList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }




        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eliminate_leaderboard, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
