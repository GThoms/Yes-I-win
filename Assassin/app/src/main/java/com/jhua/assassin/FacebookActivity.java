/**
 * http://stackoverflow.com/questions/11177009/how-to-use-dpi-scaling-factor-float-to-set-margins-int
 */
package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.ParseFacebookUtils;

import bolts.Task;


//Allows the user to log in through facebook
public class FacebookActivity extends Activity {

    //Login Button
    private Button login;

    //Fonts
    private Typeface font_light;
    private Typeface font_med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        //PRETTY FONTS
        font_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        font_med = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        //PRETTY FONTS
        TextView notlog = (TextView) findViewById(R.id.fb_text);
        notlog.setTypeface(font_light);

        //PRETTY FONTS
        login = (Button) findViewById(R.id.login_button);
        login.setTypeface(font_med);

        //If login button pressed, show depressed/unpressed states of button
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) login.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(32);
                    login.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(20);
                    lp.height = px(80);
                    login.setLayoutParams(lp);
                }
                return false;
            }
        });


        //If button clicked, log in through facebook
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Login!
                login();

                // subscribe to their own channel to get push notifications from assassin
                // ParsePush.subscribeInBackground(ParseUser.getCurrentUser().getObjectId());

                // start service to get location
                Intent intent = new Intent(FacebookActivity.this, LocationService.class);
                startService(intent);

                // back to main activity if we are logged in now
                if (ParseUser.getCurrentUser() != null) {
                    finishActivity(MainActivity.LOGIN_TRUE);
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Set up option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //Opens settings if pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    //Change dp into pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

    //Login to Facebook
    private void login() {
        // fb login here
    }
}
