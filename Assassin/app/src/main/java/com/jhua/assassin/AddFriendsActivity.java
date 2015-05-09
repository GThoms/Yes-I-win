package com.jhua.assassin;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

public class AddFriendsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        final ListView friendList = (ListView) findViewById(R.id.friendList);

        //Get friend list from current user
        ArrayList<String> sFriends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");
        ArrayList<Friend> friends = new ArrayList<Friend>();

        for(String s: sFriends) {
            Friend f = new Friend(s,false);
            friends.add(f);
        }

        //Make a new adapter for friend list
        final CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.list_item_checkbox,friends);

        friendList.setAdapter(adapter);
        //If user has friends, set adapter to friend list view
        if (friends != null) {
            adapter.addAll(friends);
        }

        Button addFriends = (Button) findViewById(R.id.addfriendsButton);
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Friend> friends = adapter.getFriends();
                //send push notifications to the users
                //add the game to all friends pending games section
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friends, menu);
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


    private class CustomAdapter extends ArrayAdapter<Friend> {

        private ArrayList<Friend> friends;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Friend> fList) {
            super(context, textViewResourceId, fList);
            this.friends = new ArrayList<Friend>();
            this.friends.addAll(fList);
        }

        public CustomAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            View row = convertView;

            if (row == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.list_item_checkbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.friend_checkbox);
                row.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Friend f = (Friend) cb.getTag();
                        f.setSelected(cb.isChecked());
                    }
                });

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Friend f = friends.get(position);
            holder.name.setText(f.getName());
            holder.name.setChecked(f.isSelected());
            holder.name.setTag(f);


            return row;

        }

        public void addALL(ArrayList<Friend> frnds) {
            this.friends.addAll(frnds);
        }

        public ArrayList<Friend> getFriends() {
            return friends;
        }
    }
}
