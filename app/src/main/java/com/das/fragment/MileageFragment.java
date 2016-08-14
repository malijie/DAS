package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.chart.MileageBarChart;
import com.das.constants.MsgConstant;
import com.das.control.TrainControl;
import com.das.util.Utils;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

/**
 * Created by malijie on 2016/8/3.
 */
public class MileageFragment extends Fragment {
    private static final int UPDATE_MILEAGE_INTERVAL = 30 * 1000;

    private BarChart mMileageBarchart = null;
    private TrainControl mTrainControl = null;
    private MileageBarChart mMileageBarChart = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mileage, container,false);
        initViews(view);
        initData();
        mMileageHandler.sendEmptyMessage(MsgConstant.MSG_UPDATE_BARCHART_MILEAGE);
        return view;
    }


    private void initData() {
        mMileageBarChart = new MileageBarChart(mMileageBarchart);
        mMileageBarChart.showBarChart(mMileageBarChart.getBarData());

        mTrainControl = TrainControl.getInstance();
    }

    private void initViews(View view) {
        mMileageBarchart = (BarChart) view.findViewById(R.id.id_mileage_barchar);

    }

    private Handler mMileageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_UPDATE_BARCHART_MILEAGE:
                    double mileage = Utils.convertM2kM(mTrainControl.getTotalMileage());
                    mMileageBarChart.updateCurrentMileageLine((float)mileage);
                    sendEmptyMessageDelayed(MsgConstant.MSG_UPDATE_BARCHART_MILEAGE,UPDATE_MILEAGE_INTERVAL);
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        mMileageHandler.removeMessages(MsgConstant.MSG_UPDATE_BARCHART_MILEAGE);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
