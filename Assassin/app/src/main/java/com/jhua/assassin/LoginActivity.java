package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    Button login_button;
    Button sign_up;
    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        username = (EditText) findViewById(R.id.login_user);
        password = (EditText) findViewById(R.id.login_password);

        login_button = (Button) findViewById(R.id.login_button);
        sign_up = (Button) findViewById(R.id.sign_up_button);
        buttonListeners();
    }

    public void login() {
        String user = username.getText().toString();
        String pass = password.getText().toString();


        Toast.makeText(getApplicationContext(), "Logging in, please wait....", Toast.LENGTH_LONG).show();
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Username and/or password is incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    //Listeners for XML buttons
    private void buttonListeners() {

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent su = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(su);
                finish();
            }
        });

        //When press add, show button motions
        login_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) login_button.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(60);
                    lp.topMargin = px(30);
                    login_button.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(20);
                    lp.height = px(70);
                    login_button.setLayoutParams(lp);
                }
                return false;
            }
        });

        //When start is pressed, show button depressed/unpressed
       sign_up.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sign_up.getLayoutParams();
               if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   lp.height = px(60);
                   lp.topMargin = px(40);
                   sign_up.setLayoutParams(lp);
               }
               if (event.getAction() == MotionEvent.ACTION_UP) {
                   lp.topMargin = px(30);
                   lp.height = px(70);
                   sign_up.setLayoutParams(lp);
               }
               return false;
           }
       });
    }

    //Turns dps to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

}