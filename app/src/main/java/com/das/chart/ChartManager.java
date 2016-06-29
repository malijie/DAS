package com.das.chart;

import android.graphics.Color;

import com.das.Myapp;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * Created by malijie on 2016/6/28.
 */
public class ChartManager {
    private static ChartManager sChatManager = null;
    private static final Object sObject = new Object();

    private BarData mBarData;

    private ChartManager(){

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

    /**
     * 生成一个能耗图
     * @param energyBarChart
     */
    public void createEnergyBarChart(BarChart energyBarChart){
        mBarData = getEnergyBarChartData();
        showEnergyBarChart(energyBarChart,mBarData);
    }

    /**
     * 生成一个运行曲线
     * @param mLineChart
     */
    public void createRunningCurveChart(LineChart mLineChart){
        LineData mLineData = getLineData(36, 2);
        showLineChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
    }

    /**
     * 生成一个数据
     * @param count 表示图表中有多少个坐标点
     * @param range 用来生成range以内的随机数
     * @return
     */
    private LineData getLineData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            yValues.add(new Entry(150, i));
        }

        // create a data set and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "距离" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
        lineDataSet.setCubicIntensity(0.6f);//设置平滑度
        lineDataSet.setDrawFilled(true);//允许填充
        lineDataSet.setDrawCubic(true);//设置曲线平滑
        lineDataSet.setDrawCircles(false);//不显示小圆点


        // create a data set and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet2 = new LineDataSet(yValues, "距离" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet2.setLineWidth(1.75f); // 线宽
        lineDataSet2.setCircleSize(3f);// 显示的圆形大小
        lineDataSet2.setColor(Color.YELLOW);// 显示颜色
        lineDataSet2.setCircleColor(Color.YELLOW);// 圆形的颜色
        lineDataSet2.setHighLightColor(Color.YELLOW); // 高亮的线的颜色
        lineDataSet2.setCubicIntensity(0.6f);//设置平滑度
        lineDataSet2.setDrawFilled(true);//允许填充
        lineDataSet2.setDrawCubic(true);//设置曲线平滑
        lineDataSet2.setDrawCircles(false);//不显示小圆点

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        lineDataSets.add(lineDataSet2);



        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);


        return lineData;
    }

    // 设置曲线显示的样式
    private void showLineChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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

//      barChart.setBackgroundColor();// 设置背景

        //设置限制线
        barChart.setDrawBarShadow(true);
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
