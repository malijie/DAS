package com.das.manager;

import android.app.Activity;
import android.content.Intent;

import com.das.Myapp;
import com.das.constants.Constants;

/**
 * Created by Administrator on 2016/6/26.
 */
public class IntentManager {
    public static void startActivity(Class activity){
        Intent i = new Intent(Myapp.sContext,activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Myapp.sContext.startActivity(i);
    }

    public static void startService(Class service,String action){
        Intent i = new Intent(Myapp.sContext,service);
        i.setAction(action);
        Myapp.sContext.startService(i);
    }

    public static void sendBroadcastMsg(String action,String key,int value){
        Intent i = new Intent(action);
        i.putExtra(key,value);
        Myapp.sContext.sendBroadcast(i);
    }

    public static void sendBroadcastMsg(String action,String key,double value){
        Intent i = new Intent(action);
        i.putExtra(key,value);
        Myapp.sContext.sendBroadcast(i);
    }

    public static void sendBroadcastMsg(String action){
        Intent i = new Intent(action);
        Myapp.sContext.sendBroadcast(i);
    }


}
