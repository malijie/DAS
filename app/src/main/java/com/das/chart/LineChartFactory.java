package com.das.chart;

import android.graphics.Color;

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

import data.DataManager;

/**
 * Created by Administrator on 2016/6/30.
 */
public class LineChartFactory {

    private static final Object sObject = new Object();
    private static LineChartFactory factory = null;
    private BarData mBarData;

    private LineChartFactory(){

    }

    public static LineChartFactory getInstance(){
        if (factory == null){
            synchronized (sObject){
                if(factory == null){
                    factory = new LineChartFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 生成一个运行曲线
     * @param mLineChart
     */
    public void createRunningCurveChart(LineChart mLineChart){
        LineData mLineData = getLineData(ChartConstants.RUNNING_DISTANCE);
        showLineChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
    }

    /**
     * 生成一个数据
     * @param count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData(int count) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }

        LineDataSet suggestSpeedLineDataSet = getSuggestSpeedLineDataSet();
        LineDataSet topSpeedDataSet = getTopSpeedLineDataSet();

        ArrayList<ILineDataSet> allLinesList = new ArrayList<ILineDataSet>();
        allLinesList.add(topSpeedDataSet); // add the datasets
        allLinesList.add(suggestSpeedLineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, allLinesList);
        lineData.setDrawValues(false);

        return lineData;
    }

    private void setCurrentSpeedLineDataSet(XAxis xAxis) {
        LimitLine xLimitLine = new LimitLine(4f,"xL 测试");
        xLimitLine.setLineColor(Color.GREEN);
        xLimitLine.setTextColor(Color.GREEN);
        xAxis.addLimitLine(xLimitLine);
    }

    /**
     * 获取建议速度值得集合
     * @return
     */
    private LineDataSet getSuggestSpeedLineDataSet(){
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        List<Integer> speedList = DataManager.getInstance().getSuggestSpeedList();
        for (int i = 0; i < speedList.size(); i++) {
            float value = (float) speedList.get(i);
            yValues.add(new Entry(value, i));
        }
        LineDataSet lineSpeedDataSet = new LineDataSet(yValues, "距离");
        lineSpeedDataSet.setLineWidth(1.75f); // 线宽
        lineSpeedDataSet.setCircleSize(3f);// 显示的圆形大小
        lineSpeedDataSet.setColor(Color.GREEN);// 显示颜色
        lineSpeedDataSet.setCircleColor(Color.GREEN);// 圆形的颜色
        lineSpeedDataSet.setHighLightColor(Color.GREEN); // 高亮的线的颜色
        lineSpeedDataSet.setCubicIntensity(1f);//设置平滑度
        lineSpeedDataSet.setDrawFilled(true);//允许填充
        lineSpeedDataSet.setDrawCubic(false);//设置曲线平滑
        lineSpeedDataSet.setDrawCircles(false);//不显示小圆点
        return lineSpeedDataSet;
    }

    /**
     * 获取最高速度值得集合
     * @return
     */
    private LineDataSet getTopSpeedLineDataSet(){
        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        List<Integer> speedList = DataManager.getInstance().getSafeSpeedList();
        for (int i = 0; i < speedList.size(); i++) {
            float value = (float) speedList.get(i);
            yValues.add(new Entry(value, i));
        }

        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "距离");
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.BLACK);// 显示颜色
        lineDataSet.setCircleColor(Color.BLACK);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.BLACK); // 高亮的线的颜色
        lineDataSet.setCubicIntensity(1f);//设置平滑度
        lineDataSet.setDrawFilled(true);//允许填充
        lineDataSet.setDrawCubic(false);//设置曲线平滑
        lineDataSet.setDrawCircles(false);//不显示小圆点

        return lineDataSet;
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
        lineChart.setScaleEnabled(false);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormToTextSpace(10f);
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setLabelCount(220,false);
        // add data
        lineChart.setData(lineData); // 设置数据
        setCurrentSpeedLineDataSet(xAxis);//设置当前位置

    }
}
