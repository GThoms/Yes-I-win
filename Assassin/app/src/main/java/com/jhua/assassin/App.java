package com.jhua.assassin;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Facebook stuff
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Parse stuff ?
        Parse.enableLocalDatastore(this);

        //Register Game as a subclass
        ParseObject.registerSubclass(Game.class);

        //Link parse to this app using keys
        Parse.initialize(this, "hxFZwmGDuKwt2BXEoyGTcPuPuFc8IJkx3eQD2DV4", "o3P37KBeAVP4970XyU0AXgrserg7qT6EEmI4J47r");

        //Initialize Facebook on this app
        ParseFacebookUtils.initialize(getApplicationContext());
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Parse Pushes
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
