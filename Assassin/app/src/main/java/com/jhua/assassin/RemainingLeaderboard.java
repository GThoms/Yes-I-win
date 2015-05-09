package com.jhua.assassin;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;


public class RemainingLeaderboard extends Activity {
    ListView winsList;
    ArrayList<String> friends;
    ArrayList<ParseUser> friendObjects;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remaining_leaderboard);

        winsList = (ListView) findViewById(R.id.listView2);

        friends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");

        friendObjects = (ArrayList<ParseUser>) ParseUser.getCurrentUser().get("friendObjects");

        adapter = new ArrayAdapter(this.getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, friends) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                if (friends != null && friendObjects != null) {
                    text1.setText("Games Won: " + friendObjects.get(position).get("wins").toString());
                    text1.setTextColor(0xff000000);

                    text1.setTextSize(20);
                    text2.setText(friends.get(position));
                    text2.setTextColor(0xff000000);
                    text2.setTextSize(20);
                }
                return view;

            }
        };

        //If user has friends, set adapter to friend list view
        if (friends != null) {
            winsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        if (friends != null) {
            winsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            winsList.invalidateViews();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remaining_leaderboard, menu);
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
