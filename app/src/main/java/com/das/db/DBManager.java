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

    public double[][] read2DArrayFromTable(String tableName){

        Cursor result = db.rawQuery(SQLContainer.getSelectAllSQL() + tableName,null);
        double resultArray[][] = new double[result.getCount()][2];
        for(int i=0;result.moveToNext();i++){

            resultArray[i][0] = Float.parseFloat(result.getString(0));
            resultArray[i][1] = Float.parseFloat(result.getString(1));
        }

        return resultArray;
    }

    public double[] read1DArrayFromTable(String tableName){
        Cursor result = db.rawQuery(SQLContainer.getSelectAllSQL() + tableName,null);
        double resultArray[] = new double[result.getCount()];
        for(int i=0;result.moveToNext();i++){
            resultArray[i] = Float.parseFloat(result.getString(0));
        }
        return resultArray;
    }


}
