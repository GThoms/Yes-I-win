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

import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.ParseFacebookUtils;

import bolts.Task;

public class LogoutActivity extends Activity {

    private Button logout;
    private Typeface font_light;
    private Typeface font_med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        font_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        font_med = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        TextView notlog = (TextView) findViewById(R.id.fb_text);
        notlog.setTypeface(font_light);

        logout = (Button) findViewById(R.id.logout_button);
        logout.setTypeface(font_med);

        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logout.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(68);
                    lp.topMargin = px(32);
                    logout.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(20);
                    lp.height = px(80);
                    logout.setLayoutParams(lp);
                }
                return false;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

                // stop service to get location
                Intent service = new Intent(LogoutActivity.this, LocationService.class);
                stopService(service);

                // back to login activity
                if (ParseUser.getCurrentUser() == null) {
                    Intent login = new Intent(LogoutActivity.this, FacebookActivity.class);
                    startActivity(login);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

    private void logout() {
        // fb logout here
        ParseUser.logOut();
    }
}
