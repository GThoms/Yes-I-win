package com.jhua.assassin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends Activity {

    ListView lv;
    ArrayList<Friend> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        //Get friend list from current user
        ArrayList<String> sFriends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");


        friends = new ArrayList<Friend>();
        lv = (ListView) findViewById(R.id.addfriendList);

        for (String s : sFriends) {
            Friend f = new Friend(s, false);
            friends.add(f);
        }

        //Make a new adapter for friend list
        final CustomAdapter adapter = new CustomAdapter(this, friends);
        lv.setAdapter(adapter);
        //If user has friends, set adapter to friend list view

        Button addFriends = (Button) findViewById(R.id.addfriendsButton);
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selected = adapter.getSelected();
                Intent intent = new Intent(AddFriendsActivity.this, CreateGameActivity.class);
                intent.putStringArrayListExtra("PLAYER_LIST", selected);
                startActivity(intent);
                //send push notifications to the users
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


    public class CustomAdapter extends ArrayAdapter<Friend> {

        private ArrayList<Friend> friends;
        private Context context;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Friend> fList) {
            super(context, textViewResourceId, fList);
            this.friends = new ArrayList<Friend>();
            this.friends.addAll(fList);
        }

        public CustomAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public CustomAdapter(Context context, ArrayList<Friend> friendList) {
            super(context, R.layout.list_item_checkbox, friendList);
            this.friends = friendList;
            this.context = context;
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();
            View row = convertView;

            if (row == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.list_item_checkbox, null);

                holder.name = (CheckBox) row.findViewById(R.id.friend_checkbox);
                row.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Friend friend = (Friend) cb.getTag();
                        friend.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) row.getTag();
            }

            Friend f = friends.get(position);
            holder.name.setText(f.getName());
            holder.name.setChecked(f.isSelected());
            holder.name.setTag(f);


            return row;

        }

        public void addAll(ArrayList<Friend> frnds) {
            this.friends.addAll(frnds);
        }

        public ArrayList<Friend> getFriends() {
            return friends;
        }

        public ArrayList<String> getSelected() {
            ArrayList<String> selected = new ArrayList<String>();
            for(Friend f: friends) {
                if(f.isSelected()) {
                    selected.add(f.getName());
                }
            }
            return selected;
        }
    }
}
