package com.das.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.chart.EnergySpeedChart;
import com.das.constants.IntentConstants;
import com.das.control.TrainControl;
import com.das.manager.ToastManager;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowSpeedFragment extends Fragment{
    private static final String TAG = ShowSpeedFragment.class.getSimpleName();
    private TextView mTextLimitSpeed = null;
    private TextView mTextSuggestSpeed = null;
    private TextView mTextCurrentSpeed = null;
    private BarChart mBarChartSuggestSpeed = null;
    private TrainControl mTrainControl = null;
    private BarData mBarData;

    private EnergySpeedChart mEnergySpeedChart = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_showspeed, container,false);

        initViews(v);
        initData();
        initChart();

        return v;
    }

    private void initChart() {
        mEnergySpeedChart.createSpeedBarChart(mBarChartSuggestSpeed,150);
    }

    private void initData() {
        mEnergySpeedChart = EnergySpeedChart.getInstance();
        mTrainControl = TrainControl.getInstance();

        mTextCurrentSpeed.setText(mTrainControl.getCurrentSpeed() + "");

        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_TRAIN_LIMIT_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED);
        getActivity().registerReceiver(mSpeedReceiver,filter);

    }

    private void initViews(View v) {
        mTextCurrentSpeed = (TextView) v.findViewById(R.id.id_speed_text_current_speed);
        mBarChartSuggestSpeed = (BarChart)v.findViewById(R.id.id_speed_bar_chart_suggest_speed);
        mTextSuggestSpeed = (TextView)v.findViewById(R.id.id_speed_text_suggest_value);
        mTextLimitSpeed = (TextView) v.findViewById(R.id.id_speed_text_limit_value);
    }

    private BroadcastReceiver mSpeedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(IntentConstants.ACTION_UPDATE_CURRENT_SPEED)){
                mTextCurrentSpeed.setText(mTrainControl.getCurrentSpeed() + "");
                updateSpeed(mTrainControl.getCurrentSpeed());
            }else if(action.equals(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED)){
                mTextSuggestSpeed.setText(intent.getDoubleExtra("suggest_velocity",0) + "");
                mEnergySpeedChart.updateSuggestSpeedLine((float)mTrainControl.getSuggestSpeed());
            }else if(action.equals(IntentConstants.ACTION_UPDATE_TRAIN_LIMIT_SPEED)){
                mTextLimitSpeed.setText(intent.getDoubleExtra("limit_velocity",0) + "");
                mEnergySpeedChart.updateLimitSpeedLine((float)mTrainControl.getLimitSpeed());
            }
        }
    };

    private void updateSpeed(int speed){
        mEnergySpeedChart.updateSpeed(speed);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mSpeedReceiver);
    }
}
