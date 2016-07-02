package com.das.data;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.das.Myapp;

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

    public DASLocationManager(){
        mLocationManager = (LocationManager) Myapp.sContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = LocationManager.GPS_PROVIDER;
    }


    public double getLatitude(){
        return mLocationManager.getLastKnownLocation(mLocationProvider).getLatitude();
    }

    public double getLongtitude(){
        return mLocationManager.getLastKnownLocation(mLocationProvider).getLongitude();
    }


}
