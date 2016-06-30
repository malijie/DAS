package com.das.chart;

import android.graphics.Color;

import com.das.Myapp;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/30.
 */
public class BarChartFactory {
    private static final Object sObject = new Object();
    private static BarChartFactory factory = null;
    private BarData mBarData;

    private BarChartFactory(){

    }

    public static BarChartFactory getInstance(){
        if (factory == null){
            synchronized (sObject){
                if(factory == null){
                    factory = new BarChartFactory();
                }
            }
        }
        return factory;
    }


    /**
     * 生成一个能耗图
     * @param energyBarChart
     */
    public void createEnergyBarChart(BarChart energyBarChart){
        mBarData = getEnergyBarChartData();
        showEnergyBarChart(energyBarChart,mBarData);
    }

    private BarData getEnergyBarChartData() {
        //X轴表示的含义
        ArrayList<String> xValues = new ArrayList<String>();
        xValues.add("");

        //Y轴表示的含义
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(170f, 0));
        //TODO 170表示当前的速度，后期经过测速来赋值

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");

        barDataSet.setColor(Color.rgb(118, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    private void showEnergyBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框
        barChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//
        barChart.setDrawBarShadow(true);  //设置限制线
        LimitLine limitLine = new LimitLine(ChartConstants.SPEED_LIMIT,"");

        //Y轴左边刻度从0-220，限制线为180
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMaxValue(ChartConstants.SPEED_LIMIT_UP);
        leftAxis.setAxisMinValue(ChartConstants.SPEED_LIMIT_DOWN);
        //刻度间距
        leftAxis.addLimitLine(limitLine);
        leftAxis.setLabelCount(ChartConstants.SPEED_LEVEL,false);

        //不显示右边Y轴
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Myapp.sContext.getResources().getColor(R.color.white));
        rightAxis.setTextSize(16f);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

        barChart.animateX(2500); // 立即执行的动画,x轴
    }
}
