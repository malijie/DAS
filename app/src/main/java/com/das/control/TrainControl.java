package com.das.control;

import com.das.data.DataConstants;
import com.das.manager.BaiduLocationManager;
import com.das.util.Logger;

/**
 * Created by Administrator on 2016/7/2.
 */
public class TrainControl {
    private static final Object sObject = new Object();

    private static TrainControl mTrainControl = null;
    private static final double EARTH_RADIUS = 6378137.0;
    private BaiduLocationManager mLocationManager = null;

    private TrainControl(){
        mLocationManager = BaiduLocationManager.getInstance();
    }

    public static TrainControl getInstance(){
        if (mTrainControl == null){
            synchronized (sObject){
                if(mTrainControl == null){
                    mTrainControl = new TrainControl();
                }
            }
        }
        return mTrainControl;
    }

    public int getCurrentSpeed(){
       return (int)mLocationManager.getCurrentSpeed();
    }

    public double getTotalDistance(double srcLat,double srcLong,double desLat,double desLong){
        double Lat1 = rad(srcLat);
        double Lat2 = rad(desLat);
        double a = Lat1 - Lat2;
        double b = rad(srcLong) - rad(desLong);
        double s = (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(Lat1) * Math.cos(Lat2)
                        * Math.pow(Math.sin(b / 2), 2))));
        s = (s * EARTH_RADIUS);
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private double rad(double d) {
        return (d * Math.PI / 180.0);
    }

    public double getCurrentLatitude(){
        return mLocationManager.getCurrentLatitude();
    }

    public double getCurrentLongitude(){
        return mLocationManager.getCurrentLongitude();
    }

}
