package com.das;

import android.app.Application;
import android.content.Context;

import com.das.file.FileManager;
import com.das.listener.SpeedListener;
import com.das.util.Logger;

/**
 * Created by 程 on 2016/4/7.
 */

//全局变量

public class Myapp extends Application{
     public static Context sContext = null;

//    private SpeedListener listener;
//    private float myspeed=0;
//
//    public float getMyspeed(){
//        return myspeed;
//    }
//
//    public void setMyspeed(float myspeed1){
//        myspeed=myspeed1;
//
//        //listener.notifySpeed(myspeed);
//    }
//
//    public Myapp(){
//
//    }
//
//    public Myapp(SpeedListener listener) {
//        this.listener = listener;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("MLJ","application onCreate");
        sContext = getApplicationContext();
        Logger.setLogSwitch(true);
    }
}
