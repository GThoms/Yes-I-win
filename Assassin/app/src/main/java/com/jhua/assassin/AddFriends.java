package com.jhua.assassin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class AddFriends extends Activity {
    boolean found;
    EditText friendName;
    Button addFriends;
    String player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        addFriends = (Button) findViewById(R.id.addfriends);
        final ListView friendList = (ListView) findViewById(R.id.list_friends);

        ArrayList<String> friends = (ArrayList<String>)ParseUser.getCurrentUser().get("friends");

        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item_checkbox,
                android.R.id.text1, friends);

        if (friends != null) {
            friendList.setAdapter(adapter);
        }

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = friendList.getAdapter().getCount();
                for(int x = 0; x < count; x++) {
                    adapter.getItem(x);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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

    private class MyCustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> userList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<String> userList) {
            super(context, textViewResourceId, userList);
            this.userList = new ArrayList<String>();
            this.userList.addAll(userList);
        }

        private class ViewHolder {
            TextView uName;
            CheckBox check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_item_checkbox, null);

                holder = new ViewHolder();
                holder.uName = (TextView) convertView.findViewById(R.id.text1);
                holder.check = (CheckBox) convertView.findViewById(R.id.friend_checkbox);
                convertView.setTag(holder);

                holder.check.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            String user = userList.get(position);
            holder.uName.setText(user);
            //holder.check.setText(country.getName());
            //holder.check.setChecked(user.isSelected());
            //holder.check.setTag(country);

            return convertView;

        }

    }
}
