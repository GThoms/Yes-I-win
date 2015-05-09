package com.jhua.assassin;

import android.app.Activity;
<<<<<<< HEAD
import android.content.Context;
=======
>>>>>>> 2183c98cb636d71834ab847806a634b512de1bc2
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

<<<<<<< HEAD
import java.util.ArrayList;


=======
>>>>>>> 2183c98cb636d71834ab847806a634b512de1bc2
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
        final CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.list_item_checkbox, android.R.id.text1,friends);

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

        public CustomAdapter(Context context, int resource, int textViewResourceId, ArrayList<Friend> fList) {
            super(context, resource, textViewResourceId, fList);
            this.friends = new ArrayList<Friend>();
            this.friends.addAll(fList);
        }

        private class ViewHolder {
            TextView name;
            CheckBox check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            View row = convertView;

            if (row == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.list_item_checkbox, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.text1);
                holder.check = (CheckBox) convertView.findViewById(R.id.friend_checkbox);
                row.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Friend f = (Friend) cb.getTag();
                        f.setSelected(cb.isChecked());
                    }
                });

                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
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
            holder.check.setText(f.getName());
            holder.check.setChecked(f.isSelected());
            holder.check.setTag(f);


            return row;

        }

        public ArrayList<Friend> getFriends() {
            return friends;
        }
    }
}
