/* adapted from http://javapapers.com/android/android-location-fused-provider/ */
package com.jhua.assassin;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


//Stars Google API GPS
public class LocationService extends Service implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    //Google API Client Variables
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;



    //Create location request and set settings
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        //High accuracy for locations
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate ...............................");

        //Create location request
        createLocationRequest();

        //Google API Client Builder, provides methods we can use from Google API
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                //API for location services
                .addApi(LocationServices.API)

                //Necessary for Google API to work properly
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    //Connect to Google API in background
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }


    //Stop connection to Google API if service is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }


    //Connected to Google API services, start location updates
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }


    protected void startLocationUpdates() {


        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }


    //Suspended from google API services
    @Override
    public void onConnectionSuspended(int i) {

    }


    //Connection to Google API failed
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    //Saved changed location of the user, saves in parse object
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        mCurrentLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //ParseUser.getCurrentUser().put("latitude", latitude);
        //ParseUser.getCurrentUser().put("longitude", longitude);
        //saveInBackground();
        Log.d("Latitude", location.getLatitude() + "");
        Log.d("Longitude", location.getLongitude() + "");
    }

    //Stop location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
