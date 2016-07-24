package com.das.chart;

import android.graphics.Color;

import com.das.control.TrainConstants;
import com.das.util.Logger;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class RunningSpeedLineChartManager {

    private static final Object sObject = new Object();
    private static RunningSpeedLineChartManager manager = null;
    //左Y轴数据等分个数
    private static final int LEFT_YAXIS_SPACE = 20;
    private static final int MAX_SPEED =220;
    private static final int MINX_SPEED = 0;

    private LineData mLineData = null;
    private LineChart mLineChart = null;
    private XAxis mXAxis = null;
    private ArrayList<Entry> ySuggestSpeedValues = new ArrayList<>();
    private ArrayList<Entry> yLimitSpeedValues = new ArrayList<>();

    private ArrayList<String> xValues = new ArrayList<>();
    private LineDataSet mSuggestSpeedLineDataSet = null;
    private LineDataSet mLimitSpeedLineDataSet = null;


    public RunningSpeedLineChartManager(LineChart lineChart){
        mLineData = new LineData();
        mLineChart = lineChart;
        initChartConfig();
        initXAxis();
        initRightYAxis();
        initLeftYAxis();
    }


    private void initChartConfig(){
        mLineChart.setDrawBorders(false);  //是否在折线图上添加边框
        // no description text
        mLineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        mLineChart.setDrawGridBackground(false); // 是否显示表格颜色
        mLineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        mLineChart.setTouchEnabled(false); // 设置是否可以触摸

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);// 是否可以拖拽
        mLineChart.setScaleEnabled(false);// 是否可以缩放
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);//

        mLineChart.setBackgroundColor(Color.rgb(114, 188, 223));// 设置背景

        // get the legend (only possible after setting data)
        Legend mLegend = mLineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormToTextSpace(10f);
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        mLineChart.animateX(1000); // 立即执行的动画,x轴

    }

    private void initXAxis(){
        mXAxis = mLineChart.getXAxis();
        mXAxis.setAxisMaxValue(TrainConstants.TOTAL_MILEAGE);
        mXAxis.setAxisMinValue(0);
        mXAxis.setLabelsToSkip(14);
        mXAxis.setSpaceBetweenLabels(3);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void initRightYAxis(){
        YAxis rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
    }

    private void initLeftYAxis(){
        YAxis leftYAxis = mLineChart.getAxisLeft();
        leftYAxis.setLabelCount(LEFT_YAXIS_SPACE,false);
        leftYAxis.setAxisMaxValue(MAX_SPEED);
        leftYAxis.setAxisMinValue(MINX_SPEED);
    }

    public void initLineData(){
//        mLineData.clearValues();
//        ySuggestSpeedValues.clear();
//        yLimitSpeedValues.clear();
//        xValues.clear();
        mLineData.setXVals(getXValue(TrainConstants.TOTAL_MILEAGE));
        mLineData.addDataSet(getSuggestSpeedLineDataSet());
        mLineData.addDataSet(getLimitSpeedLineDataSet());
        mLineData.setDrawValues(false);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }

    private LineDataSet getSuggestSpeedLineDataSet() {
        mSuggestSpeedLineDataSet =  new LineDataSet(getSuggestSpeedYValues(), "建议速度");
        mSuggestSpeedLineDataSet.setLineWidth(1.75f); // 线宽
        mSuggestSpeedLineDataSet.setCircleSize(3f);// 显示的圆形大小
        mSuggestSpeedLineDataSet.setColor(Color.GREEN);// 显示颜色
        mSuggestSpeedLineDataSet.setCircleColor(Color.GREEN);// 圆形的颜色
        mSuggestSpeedLineDataSet.setHighLightColor(Color.GREEN); // 高亮的线的颜色
        mSuggestSpeedLineDataSet.setCubicIntensity(1f);//设置平滑度
        mSuggestSpeedLineDataSet.setDrawFilled(true);//允许填充
        mSuggestSpeedLineDataSet.setDrawCubic(false);//设置曲线平滑
        mSuggestSpeedLineDataSet.setDrawCircles(false);//不显示小圆点

        return mSuggestSpeedLineDataSet;
    }

    private LineDataSet getLimitSpeedLineDataSet() {
        mLimitSpeedLineDataSet =  new LineDataSet(getLimitSpeedYValues(), "限制速度");
        mLimitSpeedLineDataSet.setLineWidth(1.75f); // 线宽
        mLimitSpeedLineDataSet.setCircleSize(3f);// 显示的圆形大小
        mLimitSpeedLineDataSet.setColor(Color.RED);// 显示颜色
        mLimitSpeedLineDataSet.setCircleColor(Color.RED);// 圆形的颜色
        mLimitSpeedLineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色
        mLimitSpeedLineDataSet.setCubicIntensity(1f);//设置平滑度
        mLimitSpeedLineDataSet.setDrawFilled(true);//允许填充
        mLimitSpeedLineDataSet.setDrawCubic(false);//设置曲线平滑
        mLimitSpeedLineDataSet.setDrawCircles(false);//不显示小圆点

        return mLimitSpeedLineDataSet;
    }


    private ArrayList<String> getXValue(int count) {
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }
        return xValues;
    }

    private ArrayList<Entry> getSuggestSpeedYValues(){
        ySuggestSpeedValues.add(new Entry(1, ySuggestSpeedValues.size()));
        return ySuggestSpeedValues;
    }

    private ArrayList<Entry> getLimitSpeedYValues(){
        yLimitSpeedValues.add(new Entry(1, yLimitSpeedValues.size()));
        return yLimitSpeedValues;
    }

    public void updateSuggestSpeedYValues(float speed){
        ySuggestSpeedValues.add(new Entry(speed, ySuggestSpeedValues.size()));
        xValues.add("" + xValues.size());
        mSuggestSpeedLineDataSet.setYVals(ySuggestSpeedValues);
        mLineData.setXVals(xValues);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }

    public void updateLimitSpeedYValues(float speed){
        yLimitSpeedValues.add(new Entry(speed, yLimitSpeedValues.size()));
//        xValues.add("" + xValues.size());
        mLimitSpeedLineDataSet.setYVals(yLimitSpeedValues);
        mLineData.setXVals(xValues);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }



    public void loadHistorySuggestSpeedValues(List<Entry> suggestSpeeds){
        ySuggestSpeedValues.clear();
        for(int i=0;i<suggestSpeeds.size();i++){
            ySuggestSpeedValues.add(suggestSpeeds.get(i));
        }
        Logger.d("MLJ","ySuggestSpeedValues====" + ySuggestSpeedValues);
        mSuggestSpeedLineDataSet.setYVals(ySuggestSpeedValues);
        mLineData.addDataSet(mSuggestSpeedLineDataSet);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }

}
