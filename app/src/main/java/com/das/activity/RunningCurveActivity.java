package com.das.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.das.chart.RunningSpeedLineChartManager;
import com.example.das.R;
import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by Administrator on 2016/6/29.
 */
public class RunningCurveActivity extends Activity implements View.OnClickListener{
    private TextView mTextCurveTitle = null;
    private LineChart mLineChart = null;
    private Button mButtonBack = null;
    private RunningSpeedLineChartManager mRunningChartManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_curve);
        initViews();
        initData();
        initChart();
    }

    private void initChart() {
        mRunningChartManager.createRunningCurveChart(mLineChart);
    }

    private void initViews() {
        mLineChart = (LineChart) findViewById(R.id.id_curve_line_chart_running);
        mButtonBack = (Button) findViewById(R.id.id_curve_line_chart_back);
        mTextCurveTitle = (TextView) findViewById(R.id.id_curve_text_title);

        mTextCurveTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updetaSpeedLine();
            }
        });
    }

    private void initData(){
        mRunningChartManager = RunningSpeedLineChartManager.getInstance();

    }

    private void updetaSpeedLine(){
        mRunningChartManager.updateCurrentSpeed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_curve_line_chart_back:
                finish();
                break;
        }
    }
}
