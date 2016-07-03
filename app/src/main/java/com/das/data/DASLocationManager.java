package com.das.data;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.das.Myapp;
import com.das.util.Logger;

/**
 * Created by malijie on 2016/7/2.
 */
public class DASLocationManager {
    private static final Object sObject = new Object();
    private static DASLocationManager sManager = null;
    private LocationManager mLocationManager = null;
    private String mLocationProvider;


    public static DASLocationManager getInstance(){
        if(sManager == null){
            synchronized (sObject){
                if(sManager == null){
                    sManager = new DASLocationManager();
                }
            }
        }
        return sManager;
    }

    private DASLocationManager(){
        mLocationManager = (LocationManager) Myapp.sContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = LocationManager.GPS_PROVIDER;
    }

    public void startRequestLocationUpdates(){
        mLocationManager.requestLocationUpdates(mLocationProvider, 3000, 1, locationListener);
    }

    public double getLatitude(){
        if(mLocationManager.getLastKnownLocation(mLocationProvider) == null){
            return 0;
        }
        return mLocationManager.getLastKnownLocation(mLocationProvider).getLatitude();
    }

    public double getLongitude(){
        if(mLocationManager.getLastKnownLocation(mLocationProvider) == null){
            return 0;
        }
        return mLocationManager.getLastKnownLocation(mLocationProvider).getLongitude();
    }

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("MLJ", "onLocationChanged# lat= " + location.getLatitude() + ",lon=" + location.getLongitude());

            //如果位置发生变化,重新显示
            Myapp.sContext.sendBroadcast(new Intent("ACTION_REQUEST_LOCATION_UPDATE"));

        }
    };
}
