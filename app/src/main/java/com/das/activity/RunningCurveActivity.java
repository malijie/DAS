package com.das.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.das.chart.RunningSpeedLineChartManager;
import com.das.constants.IntentConstants;
import com.das.control.TrainControl;
import com.example.das.R;
import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by Administrator on 2016/6/29.
 */
public class RunningCurveActivity extends Activity implements View.OnClickListener{
    private LineChart mLineChart = null;
    private Button mButtonBack = null;
    private RunningSpeedLineChartManager mRunningChartManager = null;
    private TrainControl mTrainControl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_curve);
        initViews();
        initData();
        initChart();
    }

    private void initChart() {
        mRunningChartManager.initLineData();
    }

    private void initViews() {
        mLineChart = (LineChart) findViewById(R.id.id_curve_line_chart_running);
        mButtonBack = (Button) findViewById(R.id.id_curve_line_chart_back);

    }

    private void initData(){
        mRunningChartManager = new RunningSpeedLineChartManager(mLineChart);
        mTrainControl = TrainControl.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED);
        registerReceiver(mRunningSpeedReceiver,filter);
    }

    private void updateSpeedLine(int speed){
        mRunningChartManager.updateYValues(speed);
    }

    private void updateSuggestSpeedLine(double speed){
        mRunningChartManager.updateYValues((int)speed);
    }

    private BroadcastReceiver mRunningSpeedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_CURRENT_SPEED)){
                //updateSpeedLine(mTrainControl.getCurrentSpeed());
                //TODO: 更新当前速度
            }
//            else if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED)){
//                updateSuggestSpeedLine(mTrainControl.getSuggestSpeed());
//            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_curve_line_chart_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRunningSpeedReceiver);
    }
}
