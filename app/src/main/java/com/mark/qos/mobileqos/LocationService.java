package com.mark.qos.mobileqos;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by tushkevich_m on 22.11.2016.
 */

public class LocationService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    final String LOG_TAG = "myLogs";
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private final IBinder binder = new LocationServiceBinder();
    private PendingIntent pi;

    public LocationService() {
    }


    public IBinder onBind(Intent intent) {
        return binder;
    }



    public class LocationServiceBinder extends Binder {
        LocationService getLocationService() {
            return LocationService.this;
        }
    }



    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyS onCreate");

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public int onStartCommand(Intent intent, int flags, int startId) {

        pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    //   .addOnConnectionFailedListener(MyService.this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.d(LOG_TAG, "mGoogleApiClient !!! onConnectionFailed" +  connectionResult);
                        }
                    })
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
        mGoogleApiClient.connect();
        Log.d(LOG_TAG, "mGoogleApiClient.isConnected =" + mGoogleApiClient.isConnected());
        createLocationRequest();

        return super.onStartCommand(intent, flags, startId);
    }

    protected void createLocationRequest() {
        Log.d(LOG_TAG, " MyS createLocationRequest()");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void startLocationUpdates() {
        Log.d(LOG_TAG, "MyS startLocationUpdates");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "startLocationUpdates проблемы ");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }


    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "onLocationChanged ");

        Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, location);
        try {
            pi.send(LocationService.this, 2, intent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }


    protected void stopLocationUpdates() {
        Log.d(LOG_TAG, "MyS stopLocationUpdates()");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "!!! MyS onConnectionFaile");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "MyS onConnected");
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
