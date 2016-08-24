package com.das.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MileageBarChart {
    private LimitLine mCurrentMileageLine = null;
    private YAxis mRightAxis = null;
    private BarChart mBarchart = null;

    public MileageBarChart(BarChart barChart){
        this.mBarchart = barChart;
    }

    public void showBarChart(BarData barData) {

        mBarchart.setDrawBorders(false);  ////是否在折线图上添加边框

        mBarchart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        mBarchart.setNoDataTextDescription("You need to provide data for the chart.");

        mBarchart.setDrawGridBackground(false); // 是否显示表格颜色
        mBarchart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        mBarchart.setTouchEnabled(true); // 设置是否可以触摸

        mBarchart.setDragEnabled(false);// 是否可以拖拽
        mBarchart.setScaleEnabled(false);// 是否可以缩放

        mBarchart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        mBarchart.setDrawBarShadow(true);

        mBarchart.setData(barData); // 设置数据

        Legend mLegend = mBarchart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

//      X轴设定
//      XAxis xAxis = barChart.getXAxis();
//      xAxis.setPosition(XAxisPosition.BOTTOM);

        mBarchart.animateX(2500); // 立即执行的动画,x轴

        YAxis leftAxis = mBarchart.getAxisLeft();
        leftAxis.setEnabled(false);

        mRightAxis = mBarchart.getAxisRight();
        mRightAxis.setEnabled(true);
        mRightAxis.setAxisMaxValue(ChartConstants.MILEAGE_LIMIT_UP);
        mRightAxis.setAxisMinValue(ChartConstants.MILEAGE_LIMIT_DOWN);

        //添加当前里程线
        mCurrentMileageLine = new LimitLine(0,"");
        mCurrentMileageLine.setLineColor(Color.RED);
        mRightAxis.addLimitLine(mCurrentMileageLine);


    }

    public BarData getBarData() {
        ArrayList<String> xValues = new ArrayList<String>();
        xValues.add("里程表");

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(0, 0));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "列车里程表");

        barDataSet.setColor(Color.rgb(114, 188, 223));

//        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
//        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    /**
     * 更新最高限速
     * @param limitSpeed
     */
    public void updateCurrentMileageLine(float limitSpeed){
        mRightAxis.removeLimitLine(mCurrentMileageLine);
        mCurrentMileageLine = new LimitLine(limitSpeed,"");
        mRightAxis.addLimitLine(mCurrentMileageLine);
        mBarchart.invalidate();

    }

}
