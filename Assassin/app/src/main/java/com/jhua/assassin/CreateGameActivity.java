package com.jhua.assassin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jhua.assassin.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//Create a new game
public class CreateGameActivity extends Activity {

    //Navigation drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navItems;
    private ActionBarDrawerToggle mDrawerToggle;

    //PRETTY FONTS
    private Typeface font_med;
    private Typeface font_reg;

    //XML Buttons
    private Button add;
    private Button start;

    //Setting spinners
    private EditText gameTitle;
    private Spinner gameDurationSpinner;
    private Spinner blockDurationSpinner;
    private Spinner attackRadiusSpinner;

    //Settings
    private float gameDuration; // in days
    private float blockDuration; // in seconds
    private float attackRadius; // in yards

    //String array for friends
    ArrayList<String> userNames;

    //Create activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        userNames = getIntent().getStringArrayListExtra("PLAYER_LIST");

        //PRETTY FONTS
        font_med = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        font_reg = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        setFonts();

        //Navigation Drawer
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

        //Store XML Buttons
        add = (Button) findViewById(R.id.add_friends);
        add.setTypeface(font_med);
        start = (Button) findViewById(R.id.start);
        start.setTypeface(font_med);

        //Set listeners to XML buttons
        buttonListeners();

        //Default setting vaules
        attackRadius = 1;
        blockDuration = 10;
        gameDuration = 14;

        //store spinners
        gameDurationSpinner = (Spinner) findViewById(R.id.game_duration_spinner);
        blockDurationSpinner = (Spinner) findViewById(R.id.block_duration_spinner);
        attackRadiusSpinner = (Spinner) findViewById(R.id.attack_radius_spinner);

        //Set listeners to spinners
        spinnerListeners();
    }

    //Navigation toggle
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    //Navigation Toggle
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Populate settings and navigation list
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

    //If settings selected, what happens
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

    //Turns dps to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

    //Sets fonts of textviews in XML
    private void setFonts() {

        TextView game_t = (TextView) findViewById(R.id.game_title_tv);
        TextView game_d = (TextView) findViewById(R.id.game_duration_tv);
        TextView blck_d = (TextView) findViewById(R.id.block_duration_tv);
        TextView atck_r = (TextView) findViewById(R.id.textView6);

        game_t.setTypeface(font_reg);
        game_d.setTypeface(font_reg);
        blck_d.setTypeface(font_reg);
        atck_r.setTypeface(font_reg);
    }

    //Listens to spinners and reacts when selected
    private void spinnerListeners() {

        //Adapter for Game Duration Spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.game_duration, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameDurationSpinner.setAdapter(adapter1);


        //Adapter for Block Duration Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.block_duration, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockDurationSpinner.setAdapter(adapter2);

        //Adapter for attack radius Spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.attack_radius, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attackRadiusSpinner.setAdapter(adapter3);

        //Listener for Game duration spinner
        gameDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //When we select an item
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gameDuration = 7;
                        break;
                    case 1:
                        gameDuration = 14;
                        break;
                    case 2:
                        gameDuration = 30;
                        break;
                    case 3:
                        gameDuration = 0;
                        break;
                    case 4:
                        showDialog('g');
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Listener for block duration spinner
        blockDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gameDuration = 5;
                        break;
                    case 1:
                        gameDuration = 10;
                        break;
                    case 2:
                        gameDuration = 15;
                        break;
                    case 3:
                        gameDuration = 30;
                        break;
                    case 4:
                        showDialog('b');
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Listener for attack radius spinner
        attackRadiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gameDuration = 1;
                        break;
                    case 1:
                        gameDuration = 5;
                        break;
                    case 2:
                        gameDuration = 10;
                        break;
                    case 3:
                        gameDuration = 50;
                        break;
                    case 4:
                        showDialog('a');
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // comment these out if we decide to use the spinners
        TextView gdtv = (TextView) findViewById(R.id.game_duration_tv);
        TextView bdtv = (TextView) findViewById(R.id.block_duration_tv);

        bdtv.setVisibility(View.GONE);
        gdtv.setVisibility(View.GONE);
        gameDurationSpinner.setVisibility(View.GONE);
        blockDurationSpinner.setVisibility(View.GONE);
    }


    //Listeners for XML buttons
    private void buttonListeners() {

        //When press add, show button motions
        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) add.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(16);
                    add.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(10);
                    lp.height = px(80);
                    add.setLayoutParams(lp);
                }
                return false;
            }
        });

        //When add is pressed
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGameActivity.this, AddFriendsActivity.class);
                ArrayList<String> sFriends = (ArrayList<String>) ParseUser.getCurrentUser().get("friends");
                if(sFriends == null) {
                    Toast.makeText(getApplicationContext(),"You do not have any friends to add!",Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });


        //When start is pressed, show button depressed/unpressed
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) start.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(16);
                    start.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(10);
                    lp.height = px(80);
                    start.setLayoutParams(lp);
                }
                return false;
            }
        });


        //Make game, go back to home screen
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (makeGame()) {
                        Intent home = new Intent(CreateGameActivity.this, MainActivity.class);
                        startActivity(home);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //Makes the game when start button is pressed
    private boolean makeGame() throws JSONException {

        //Store game title
        gameTitle = (EditText) findViewById(R.id.game_title);
        String name = gameTitle.getText().toString();

        //Wont work if no name is inputted
        if (name == "") {
            Toast.makeText(getApplicationContext(), "Please enter a game name!", Toast.LENGTH_LONG).show();
            return false;
        }


        //Parse Game object
        final Game newGame = new Game();

        //Set relevant object fields for Game
        newGame.setGameName(name);
        newGame.setGameDuration(gameDuration);
        newGame.setBlockDuration(blockDuration);
        newGame.setAttackRadius(attackRadius);
        newGame.setStatus("current");

        //Unimplemented player adding
        // Make ArrayList<ParseUser> with all the users added via some dialog or something
        // Add this list to parse

        ArrayList<ParseUser> players = addPlayers(userNames);
        if(userNames == null || userNames.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You need at least one other player to start a game!", Toast.LENGTH_LONG).show();
            return false;
        }
        /*
        //newGame.addPlayers(players);
        newGame.addPlayer(ParseUser.getCurrentUser());
        for(String n: userNames) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", n);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        newGame.addPlayer(list.get(0));
                    }
                }
            });
        }
        */
        // put player usernames in game
        for (String n: userNames) {
            newGame.addPlayer(n);
        }

        newGame.addPlayer(ParseUser.getCurrentUser().getUsername());

        // Set targets from player list
        ArrayList<ParseUser> targets = players;

        if(players != null) {
            Log.d("players size", "" + players.size());
        } else {
            Log.d("Players size", "NULL");
        }

        // Should be not null anymore since I'm pulling from local list instead of Parse
        // Collections.shuffle(targets);
        // Sets users list of all targets in game and sets their current target
        // newGame.setTargets(targets);

        //Saves the new parse object
        newGame.saveInBackground();

        //Set current user as creator
        newGame.setCreator(ParseUser.getCurrentUser().getUsername());

        // give the player the game
        ParseUser.getCurrentUser().put("game", newGame);
        ParseUser.getCurrentUser().saveInBackground();

        target(userNames, newGame.getObjectId());
        // start the location service
        Intent intent = new Intent(CreateGameActivity.this, LocationService.class);
        startService(intent);
        return true;
    }

    private void target(ArrayList<String> users, String gameId) {
        Collections.shuffle(users);
        String user, target = "";

        for (int x = 0; x < users.size(); x++) {
            user = users.get(x);
            target = users.get((x+1) % users.size());

            if (user.equals(ParseUser.getCurrentUser())) {
                ParseUser.getCurrentUser().put("target", target);
                ParseUser.getCurrentUser().put("game", gameId);
            } else {
                // make JSON object to send
                JSONObject data = null;
                try {
                    data = new JSONObject("{\"alert\": \"You've been invited to play Assassins!\"}");
                    data.put("target", target);
                    data.put("game", gameId);
                    Log.d("JSON", "new JSON object created: " + data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // send push w/ data
                ParsePush push = new ParsePush();
                push.setChannel(user); // the user that is being assigned the target
                push.sendDataInBackground(data, ParseInstallation.getQuery(), new SendCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Push", "sent push!");
                        } else {
                            Log.d("PUSH ERROR", e.toString());
                        }
                    }
                });
            }
        }

    }

    //Allows us to use dialogs
    private void showDialog(final char type) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CreateGameActivity.this);

        View promptView;
        if (type == 'g') { // game duration (days)
            promptView = layoutInflater.inflate(R.layout.game_duration_dialog, null);
        }
        else if (type == 'b') { // block duration (seconds)
            promptView = layoutInflater.inflate(R.layout.block_duration_dialog, null);
        }
        else { // attack radius (yards)
            promptView = layoutInflater.inflate(R.layout.attack_radius_dialog, null);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateGameActivity.this);
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (type == 'g') {
                            EditText customGameDuration = (EditText) findViewById(R.id.game_duration_dialog_edit);
                            // gameDuration = Float.parseFloat(customGameDuration.getText().toString());
                        }
                        else if (type == 'b') {
                            EditText customBlockDuration = (EditText) findViewById(R.id.block_duration_dialog_edit);
                            // blockDuration = Float.parseFloat(customBlockDuration.getText().toString());
                        }
                        else {
                            EditText customAttackRadius = (EditText) findViewById(R.id.attack_radius_dialog_edit);
                            // attackRadius = Float.parseFloat(customAttackRadius.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    
    /**
     * Add players from a set list of friends, return arraylist of users
    **/
    private ArrayList<ParseUser> addPlayers(ArrayList<String> uNames) {
        if(uNames == null || uNames.isEmpty()) {
            return null;
        }
        final ArrayList<ParseUser> players = new ArrayList<ParseUser>();
        players.add(ParseUser.getCurrentUser());
        for(String name: uNames) {
            Log.d("loop begin", "" + players.size());
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", name);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        boolean added = players.add(list.get(0));
                        Log.d("Added bool", "" + added);
                        Log.d("After add", "" + players.size());
                    }
                }
            });
            Log.d("loop end", "" + players.size());
        }
        return players;
    }
}