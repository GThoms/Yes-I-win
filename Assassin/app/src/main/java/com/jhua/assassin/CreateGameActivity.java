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
import com.parse.ParseUser;


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

    //Create activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

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

        add = (Button) findViewById(R.id.add_friends);
        add.setTypeface(font_med);
        start = (Button) findViewById(R.id.start);
        start.setTypeface(font_med);
        buttonListeners();

        attackRadius = 1;
        blockDuration = 10;
        gameDuration = 14;

        gameDurationSpinner = (Spinner) findViewById(R.id.game_duration_spinner);
        blockDurationSpinner = (Spinner) findViewById(R.id.block_duration_spinner);
        attackRadiusSpinner = (Spinner) findViewById(R.id.attack_radius_spinner);
        spinnerListeners();
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

    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

    private void setFonts() {

        TextView game_t = (TextView) findViewById(R.id.textView3);
        TextView game_d = (TextView) findViewById(R.id.textView4);
        TextView blck_d = (TextView) findViewById(R.id.textView5);
        TextView atck_r = (TextView) findViewById(R.id.textView6);

        game_t.setTypeface(font_reg);
        game_d.setTypeface(font_reg);
        blck_d.setTypeface(font_reg);
        atck_r.setTypeface(font_reg);
    }

    private void spinnerListeners() {

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.game_duration, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameDurationSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.block_duration, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockDurationSpinner.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.attack_radius, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attackRadiusSpinner.setAdapter(adapter3);

        gameDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    private void buttonListeners() {

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pressed add friends", Toast.LENGTH_SHORT).show();
            }
        });

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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (makeGame()) {
                    Intent home = new Intent(CreateGameActivity.this, MainActivity.class);
                    startActivity(home);
                }
            }
        });
    }

    private boolean makeGame() {
        gameTitle = (EditText) findViewById(R.id.game_title);
        String name = gameTitle.getText().toString();
        if (name == "") {
            Toast.makeText(getApplicationContext(), "Please enter a game name!", Toast.LENGTH_LONG).show();
            return false;
        }
        Game newGame = new Game();
        newGame.setGameName(name);

        newGame.setGameDuration(gameDuration);
        newGame.setBlockDuration(blockDuration);
        newGame.setAttackRadius(attackRadius);

        newGame.addPlayer("player1");
        newGame.addPlayer("player2");

        newGame.saveInBackground();

        // now give game to player here
        // ParseUser.getCurrentUser().addUnique("games", newGame);
        // give people targets here

        // start the location service
        Intent intent = new Intent(CreateGameActivity.this, LocationService.class);
        startService(intent);
        return true;
    }

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
}