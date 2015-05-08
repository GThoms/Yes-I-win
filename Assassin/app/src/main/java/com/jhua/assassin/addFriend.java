package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import java.util.List;


public class addFriend extends Activity {
    boolean found;
    EditText friendName;
    Button addFriend;
    String player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        //friendName = (EditText) findViewById(R.id.addFriendUsername);
        //addFriend = (Button) findViewById(R.id.addFriendButton);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player = friendName.getText().toString();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", player);
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {

                            if (objects.isEmpty()) {

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

                                    Intent intent = new Intent(addFriend.this, FriendList.class);
                                    startActivity(intent);
                                }
                            }
                        } else {

                        }
                    }
                });

            }
        });

    }

    public boolean playerExists(){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", player);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.isEmpty()) {
                        Log.d("MEH", "WRONGO");
                        found = false;
                    } else {

                        Log.d("MEH", "LOLOL");
                        found = true;
                    }
                } else {

                }
            }
        });

        return found;
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
}
