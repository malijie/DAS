package com.das.chart;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by malijie on 2016/6/28.
 */
public class ChartManager {
    private static ChartManager sChatManager = null;
    private static final Object sObject = new Object();
    private LineChartFactory mLineChartFactory = null;
    private BarChartFactory mBarChartFactory = null;

    private ChartManager(){
        mLineChartFactory = LineChartFactory.getInstance();
        mBarChartFactory = BarChartFactory.getInstance();
    }

    public static ChartManager getInstance(){
        if(sChatManager == null){
            synchronized (sObject){
                if(sChatManager == null){
                    sChatManager = new ChartManager();
                }
            }
        }
        return sChatManager;
    }

    public void createEnergyBarChart(BarChart barchart){
//        mBarChartFactory.createSpeedBarChart(barchart);
    }


    public void createRunningCurveLineChart(LineChart lineChart){
        mLineChartFactory.createRunningCurveChart(lineChart);
    }


}
