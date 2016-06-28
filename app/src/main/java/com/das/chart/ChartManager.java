package com.das.chart;

/**
 * Created by malijie on 2016/6/28.
 */
public class ChartManager {
    private static ChartManager mChatManager = null;
    private static final Object sObject = new Object();

    public static ChartManager getInstance(){
        if(mChatManager == null){
            synchronized (sObject){

            }
        }
    }
}
