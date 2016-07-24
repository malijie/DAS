package com.das.chart;

import android.graphics.Color;

import com.das.control.TrainConstants;
import com.das.data.DataManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
    private ArrayList<Entry> yValues = new ArrayList<>();
    private ArrayList<String> xValues = new ArrayList<>();
    private LineDataSet mLineDataSet = null;


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
        mXAxis.setSpaceBetweenLabels(3);
        mXAxis.setAxisMaxValue(TrainConstants.TOTAL_MILEAGE);
        mXAxis.setAxisMinValue(0);
        mXAxis.setLabelsToSkip(9);
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
        mLineData.clearValues();
        yValues.clear();
        xValues.clear();
        mLineData.setXVals(getXValue(TrainConstants.TOTAL_MILEAGE));
        mLineData.addDataSet(getLineDataSet());
        mLineData.setDrawValues(false);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }

    private LineDataSet getLineDataSet() {
        mLineDataSet =  new LineDataSet(getYValues(), "距离");
        mLineDataSet.setLineWidth(1.75f); // 线宽
        mLineDataSet.setCircleSize(3f);// 显示的圆形大小
        mLineDataSet.setColor(Color.GREEN);// 显示颜色
        mLineDataSet.setCircleColor(Color.GREEN);// 圆形的颜色
        mLineDataSet.setHighLightColor(Color.GREEN); // 高亮的线的颜色
        mLineDataSet.setCubicIntensity(1f);//设置平滑度
        mLineDataSet.setDrawFilled(true);//允许填充
        mLineDataSet.setDrawCubic(false);//设置曲线平滑
        mLineDataSet.setDrawCircles(false);//不显示小圆点

        return mLineDataSet;
    }

    private ArrayList<String> getXValue(int count) {
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }
        return xValues;
    }

    private ArrayList<Entry> getYValues(){
        yValues.add(new Entry(1, yValues.size()));
        return yValues;
    }

    public void updateYValues(float speed){
        yValues.add(new Entry(speed, yValues.size()));
        xValues.add("" + xValues.size());
        mLineDataSet.setYVals(yValues);
        mLineData.setXVals(xValues);
        mLineChart.setData(mLineData);
        mLineChart.invalidate();
    }

}
