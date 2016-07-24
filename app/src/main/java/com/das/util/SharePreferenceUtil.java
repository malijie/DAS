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

}
