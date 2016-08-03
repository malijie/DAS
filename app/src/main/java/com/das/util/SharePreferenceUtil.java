package com.das.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.das.Myapp;

/**
 * Created by Administrator on 2016/7/16.
 */
public class SharePreferenceUtil {
    public static final String SP_NAME = "train_info";
    public static final String START_LOCATION_LATITUDE = "start_latitude";
    public static final String START_LOCATION_LONGITUDE = "start_longitude";
    public static final String SUGGEST_SPEED_INDEX = "suggest_speed_index";
    public static final String LIMIT_SPEED_INDEX = "limit_speed_index";
    public static final String DRIVER_NAME = "driver_name";
    public static final String TRAIN_NO = "train_no";


    public static void saveStartLatitude(float latitude){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putFloat(START_LOCATION_LATITUDE,latitude).commit();
    }

    public static void saveStartLongitude(float longitude){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putFloat(START_LOCATION_LONGITUDE,longitude).commit();
    }

    public static void saveCurrentSuggestSpeedIndex(int index){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putInt(SUGGEST_SPEED_INDEX,index).commit();
    }

    public static int loadCurrentSuggestSpeedIndex(){
        return Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(SUGGEST_SPEED_INDEX,0);
    }

    public static void saveCurrentLimitSpeedIndex(int index){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putInt(LIMIT_SPEED_INDEX,index).commit();
    }

    public static int loadCurrentLimitSpeedIndex(){
        return Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(LIMIT_SPEED_INDEX,0);
    }

    public static void saveDriverName(String driverName){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(DRIVER_NAME,driverName).commit();
    }

    public static String loadDriverName(){
        return Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(DRIVER_NAME,"");
    }

    public static void saveTrainNo(String trainNo){
        Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(TRAIN_NO,trainNo).commit();
    }


    public static String loadTrainNo(){
        return Myapp.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(TRAIN_NO,"");
    }

}
