package com.das.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.das.Myapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malijie on 2016/6/30.
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

    public void saveTheStartLatLng(double lat,double lng){
        db.execSQL(SQLContainer.getInsertStartLatLng(lat, lng));
    }

    public ArrayList<String> getStationNames(){
        Cursor result = db.rawQuery(SQLContainer.getAllStationInfo(),null);
        ArrayList<String> stations = new ArrayList<>();
        for(;result.moveToNext();){
            stations.add(result.getString(1));
        }
        return stations;
    }

    public ArrayList<Double> getStationMileages(){
        Cursor result = db.rawQuery(SQLContainer.getAllStationInfo(),null);
        ArrayList<Double> mileages = new ArrayList<>();
        for(;result.moveToNext();){
            mileages.add(Double.parseDouble(result.getString(0)));
        }
        return mileages;
    }

    public ArrayList<Long> getStationScheduleTimes(){
        Cursor result = db.rawQuery(SQLContainer.getAllStationInfo(),null);
        ArrayList<Long> scheduleTimes = new ArrayList<>();
        for(;result.moveToNext();){
            scheduleTimes.add(result.getLong(3));
        }
        return scheduleTimes;
    }

    public ArrayList<Integer> getStationWaitTimes(){
        Cursor result = db.rawQuery(SQLContainer.getAllStationInfo(),null);
        ArrayList<Integer> arriveTimes = new ArrayList<>();
        for(;result.moveToNext();){
            arriveTimes.add(result.getInt(2));
        }
        return arriveTimes;
    }

}
