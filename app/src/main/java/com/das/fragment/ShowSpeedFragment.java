package com.das.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.chart.ChartManager;
import com.das.control.TrainControl;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowSpeedFragment extends Fragment{

    private TextView mTextCurrentSpeed = null;
    private BarChart mBarChartSuggestSpeed = null;
    private TrainControl mTrainControl = null;
    private BarData mBarData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_showspeed, container,false);

        initViews(v);
        initData();

        return v;
    }

    private void initData() {
        ChartManager.getInstance().createEnergyBarChart(mBarChartSuggestSpeed);
        mTrainControl = TrainControl.getInstance();
        mTextCurrentSpeed.setText(mTrainControl.getCurrentSpeed());
    }

    private void initViews(View v) {
        mTextCurrentSpeed = (TextView) v.findViewById(R.id.id_speed_text_current_speed);
        mBarChartSuggestSpeed = (BarChart)v.findViewById(R.id.id_speed_bar_chart_suggest_speed);
    }

    private BroadcastReceiver mSpeedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("")){

            }
        }
    };

}
