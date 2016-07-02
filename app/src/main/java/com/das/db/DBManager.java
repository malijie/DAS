package com.das.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.das.Myapp;

/**
 * Created by Administrator on 2016/6/30.
 */
public class DBManager {

    private static final Object sObject = new Object();
    private static DBManager sDBManager = null;
    private SQLiteDatabase db = null;


    private DBManager(){
        db = new SQLiteHelper(Myapp.sContext).getWritableDatabase();
    }

    public static DBManager getInstance(){
        if(sDBManager == null){
            synchronized (sObject){
                if(sDBManager == null){
                    sDBManager = new DBManager();
                }
            }
        }
        return  sDBManager;
    }


}
