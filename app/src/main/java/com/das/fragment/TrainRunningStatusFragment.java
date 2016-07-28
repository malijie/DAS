package com.das.fragment;

//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.Myapp;
import com.das.constants.MsgConstant;
import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.constants.IntentConstants;
import com.example.das.R;

/**
 * Created by �� on 2016/3/15.
 */
public class TrainRunningStatusFragment extends Fragment {
    private TextView mTextCurrentStatus = null;
    private TextView mTextNextStatus = null;
    private String provider;

    private TrainControl mTrainControl = null;
    private Handler mTrainHandler = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running_status, container, false);
        initViews(view);
        initData();
        mTrainHandler.sendEmptyMessage(MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS);
        mStatusHandler.sendEmptyMessage(MsgConstant.MSG_CALCULATE_CURRENT_TOTAL_MILEAGE);
        return view;

    }

    private void initData() {
        mTrainControl = TrainControl.getInstance();
        mTrainHandler = mTrainControl.getHandler();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_TRAIN_STATUS);
        filter.addAction(IntentConstants.ACTION_GET_TRAIN_CURRENT_STATUS_STOP);
        Myapp.sContext.registerReceiver(receiver,filter);

    }

    private void initViews(View v){
        mTextCurrentStatus = (TextView) v.findViewById(R.id.currentcondition);
        mTextNextStatus = (TextView) v.findViewById(R.id.nextcondition);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentConstants.ACTION_UPDATE_CURRENT_TRAIN_STATUS)){
                if(intent.getIntExtra("train_running_status",0) == TrainConstants.TRAIN_RUN_STATUS_KEEP){
                    mTextCurrentStatus.setText("当前工况:惰行");
                }else if(intent.getIntExtra("train_running_status",0) == TrainConstants.TRAIN_RUN_STATUS_ACCLERATE){
                    mTextCurrentStatus.setText("当前工况:牵引");

                }else if(intent.getIntExtra("train_running_status",0) == TrainConstants.TRAIN_RUN_STATUS_BREAK){
                    mTextCurrentStatus.setText("当前工况:制动");
                }
            }else if(intent.getAction().equals(IntentConstants.ACTION_GET_TRAIN_CURRENT_STATUS_STOP)){
                mTextCurrentStatus.setText("列车还未运行");
            }
        }
    };

    private Handler mStatusHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_CALCULATE_CURRENT_TOTAL_MILEAGE:
                    mTextNextStatus.setText("当前总里程是:" + TrainControl.getInstance().getTotalMileage() +"米");
                    mStatusHandler.sendEmptyMessageDelayed(MsgConstant.MSG_CALCULATE_CURRENT_TOTAL_MILEAGE,1000);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTrainHandler.removeMessages(MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS);
        Myapp.sContext.unregisterReceiver(receiver);
    }
}
