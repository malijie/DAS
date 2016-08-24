package com.das.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.das.chart.RunningSpeedLineChartManager;
import com.das.constants.IntentConstants;
import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.manager.IntentManager;
import com.das.manager.ToastManager;
import com.das.service.SimulatorService;
import com.das.util.Logger;
import com.das.util.SharePreferenceUtil;
import com.das.util.Utils;
import com.example.das.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class RunningCurveActivity extends Activity implements View.OnClickListener{
    private static final String TAG = RunningCurveActivity.class.getSimpleName();
    private LineChart mLineChart = null;
    private TextView mTextTest = null;
    private Button mButtonBack = null;
    private RunningSpeedLineChartManager mRunningChartManager = null;
    private TrainControl mTrainControl = null;
    private boolean mIsFirstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_curve);
        initViews();
        initData();
        initChart();

        mRunningChartManager.updateXYAxis(mTrainControl.getSuggestSpeedArray(),
                mTrainControl.getLimitSpeedArray(),true);
//        showHistorySuggestSpeedChart();
//        showHistoryLimitSpeedChart();
    }



    private void startCalculateLimitSpeed() {
        IntentManager.startService(SimulatorService.class,
                IntentConstants.ACTION_UPDATE_RUNNING_CURVE_LIMIT_SPEED);
    }

    private void startCalculateSuggestSpeed() {
        IntentManager.startService(SimulatorService.class,
                IntentConstants.ACTION_UPDATE_RUNNING_CURVE_SUGGEST_SPEED);
    }

    private double[] historySuggestSpeed;
    private void showHistorySuggestSpeedChart() {
//        int index = SharePreferenceUtil.loadCurrentSuggestSpeedIndex();
//        Logger.d(TAG,"Suggest index=" + index);
//        if(index == 0){
//            return;
//        }
//        historySuggestSpeed = new double[index];
//        for(int i = 0; i< historySuggestSpeed.length; i++){
//            historySuggestSpeed[i] = mTrainControl.getSuggestSpeedArray()[i * 100];
//            Logger.d(TAG,"historySuggestSpeed[i]=" + i + "=" + historySuggestSpeed[i]);
//        }
//
//        List<Entry> speeds = new ArrayList<>();
//        for(int i = 0; i< historySuggestSpeed.length; i++){
//            speeds.add(new Entry((float) historySuggestSpeed[i],i));
//        }
//        mRunningChartManager.loadHistorySuggestSpeedValues(speeds);
    }

    private double[] historyLimitSpeed;

    private void showHistoryLimitSpeedChart() {
//        int index = SharePreferenceUtil.loadCurrentLimitSpeedIndex();
//        Logger.d(TAG,"Limit index=" + index);
//        if(index == 0){
//            return;
//        }
//        historyLimitSpeed = new double[index];
//        for(int i = 0; i< historyLimitSpeed.length; i++){
//            historyLimitSpeed[i] = mTrainControl.getLimitSpeedArray()[i * 100];
//            Logger.d(TAG,"historyLimitSpeed[i]=" + i + "=" + historyLimitSpeed[i]);
//        }
//
//        List<Entry> speeds = new ArrayList<>();
//        for(int i = 0; i< historyLimitSpeed.length; i++){
//            speeds.add(new Entry((float) historyLimitSpeed[i],i));
//        }
//        mRunningChartManager.loadHistoryLimitSpeedValues(speeds);
    }

    private void initChart() {
        mRunningChartManager.initLineData();
    }

    private static float mileage = 1;
    private void initViews() {
        mLineChart = (LineChart) findViewById(R.id.id_curve_line_chart_running);
        mButtonBack = (Button) findViewById(R.id.id_curve_line_chart_back);

    }

    private void initData(){
        mRunningChartManager = new RunningSpeedLineChartManager(mLineChart);
        mTrainControl = TrainControl.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_RUNNING_CURVE_SUGGEST_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_RUNNING_CURVE_LIMIT_SPEED);
        filter.addAction(IntentConstants.ACTION_TRAIN_CURVE_MILEAGE);
        registerReceiver(mRunningSpeedReceiver,filter);
    }


    private void updateSuggestSpeedLine(double speed){
        mRunningChartManager.updateSuggestSpeedYValues((int)speed);
    }

    private void updateLimitSpeedLine(double speed){
        mRunningChartManager.updateLimitSpeedYValues((int)speed);
    }


    private static float speed = 10;
    private static float mLastTotalMileage = 0;
    private float mCurrentTotalMileage = 0;

    private BroadcastReceiver mRunningSpeedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_CURRENT_SPEED)){
                if(mIsFirstStart&& mTrainControl.getCurrentSpeed()>1){
                    startCalculateSuggestSpeed();
                    startCalculateLimitSpeed();

                    mIsFirstStart = false;
                }

            }else if(intent.getAction().equals(IntentConstants.ACTION_TRAIN_CURVE_MILEAGE)){
                if(mTrainControl.getTotalMileage() > TrainConstants.TOTAL_MILEAGE * 1000){
                   return;
                }

                if(mTrainControl.getTotalMileage() < 5000){
                    //更新建议速度，限制速度
                    mRunningChartManager.updateXYAxis(mTrainControl.getSuggestSpeedArray(),
                            mTrainControl.getLimitSpeedArray(),true);
                }else{
                    if(mTrainControl.getTotalMileage() - mLastTotalMileage > 5 * 1000){
                        mLastTotalMileage = (float)mTrainControl.getTotalMileage();
                        //更新建议速度，限制速度
                        mRunningChartManager.updateXYAxis(mTrainControl.getSuggestSpeedArray(),
                                mTrainControl.getLimitSpeedArray(),false);
                    }else{
                        mRunningChartManager.updateXYAxis(mTrainControl.getSuggestSpeedArray(),
                                mTrainControl.getLimitSpeedArray(),true);
                    }
                }



                //更新速率
//                updateCurrentSpeedLine((float)Utils.convertM2kM(mTrainControl.getTotalMileage()));
                //更新当前速度
                mRunningChartManager.updateCurrentSpeedLine();

            }
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
        mIsFirstStart = true;
        unregisterReceiver(mRunningSpeedReceiver);
    }
}
