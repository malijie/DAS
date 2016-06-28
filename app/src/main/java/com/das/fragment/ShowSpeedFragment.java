package com.das.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.das.R;
import com.das.listener.SpeedListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowSpeedFragment extends Fragment implements SpeedListener {

    private TextView mTextCurrentSpeed = null;
    private BarChart mBarChartSuggestSpeed = null;
    private BarData mBarData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_showspeed, container,false);
        mTextCurrentSpeed = (TextView) view.findViewById(R.id.id_speed_text_current_speed);
        mBarChartSuggestSpeed = (BarChart)view.findViewById(R.id.id_speed_barchart_suggest_speed);

        mBarData = getBarData(1, 220);
        showBarChart(mBarChartSuggestSpeed, mBarData);

        return view;
    }

    @Override
    public void notifySpeed(float speed) {
       // showspeednumber();
        String show = speed + "km/h";
        mTextCurrentSpeed.setText(show);
    }

 /*   private void showspeednumber(){
        String show = myapp.getMyspeed() + "km/h";
        mTextCurrentSpeed.setText(show);
    }*/

    private void showBarChart(BarChart barChart, BarData barData) {
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
        LimitLine limitLine = new LimitLine(180f,"");


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMaxValue(220);
        leftAxis.setAxisMinValue(0);
        leftAxis.addLimitLine(limitLine);


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

//      X轴设定
//      XAxis xAxis = barChart.getXAxis();
//      xAxis.setPosition(XAxisPosition.BOTTOM);

        barChart.animateX(2500); // 立即执行的动画,x轴
    }

    private BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("第" + (i + 1) + "季度");
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues.add(new BarEntry(170, i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "测试饼状图");

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }


}
