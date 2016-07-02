package com.das.control;

import com.das.data.DataConstants;
import com.das.util.Logger;

/**
 * Created by Administrator on 2016/7/2.
 */
public class TrainControl {
    private static final Object sObject = new Object();
    private static TrainControl mTrainControl = null;
    private static final double EARTH_RADIUS = 6378137.0;

    private TrainControl(){

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

    public int getCurrentSpeed(float srcLat,float srcLong,float desLat,float desLong){
        Logger.d("MLJ","time=" + TrainConstants.TIME_UNIT);
        return (int) (Math.round(getTotalDistance(srcLat,srcLong,desLat,desLong)/TrainConstants.DISTANCE_UNIT)
                        /(TrainConstants.TIME_UNIT));

    }

    public float getTotalDistance(float srcLat,float srcLong,float desLat,float desLong){
        float Lat1 = rad(srcLat);
        float Lat2 = rad(desLat);
        float a = Lat1 - Lat2;
        float b = rad(srcLong) - rad(desLong);
        float s = (float) (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(Lat1) * Math.cos(Lat2)
                        * Math.pow(Math.sin(b / 2), 2))));
        s = (float) (s * EARTH_RADIUS);
        s = Math.round(s * 10000) / 10000;
        Logger.d("MLJ","getTotalDistance=" + s);
        return s;
    }

    private float rad(float d) {
        return (float) (d * Math.PI / 180.0);
    }

}
