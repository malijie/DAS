package com.das.manager;

import android.app.Activity;
import android.content.Intent;

import com.das.Myapp;

/**
 * Created by Administrator on 2016/6/26.
 */
public class IntentManager {
    public static void startActivity(Class activity){
        Intent i = new Intent(Myapp.sContext,activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Myapp.sContext.startActivity(i);
    }

    public static void startService(Class service){
        Intent i = new Intent(Myapp.sContext,service);
        Myapp.sContext.startService(i);
    }


}
