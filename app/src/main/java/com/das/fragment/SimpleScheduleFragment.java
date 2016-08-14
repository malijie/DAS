package com.das.fragment;

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
import com.das.constants.IntentConstants;
import com.das.constants.MsgConstant;
import com.das.control.TrainControl;
import com.das.db.DBManager;
import com.das.util.Utils;
import com.example.das.R;

import java.util.ArrayList;

/**
 * Created by �� on 2016/3/16.
 */
public class SimpleScheduleFragment extends Fragment {
    private TextView mTextPreStation = null;
    private TextView mTextPreWaitTime = null;
    private TextView mTextPreArriveTime = null;
    private TextView mTextPreMileage = null;
    private TextView mTextNextWaitTime = null;
    private TextView mTextNextStation = null;
    private TextView mTextNextArriveTime = null;
    private TextView mTextNextSchedule = null;
    private TextView mTextNextMileage = null;

    private DBManager mDBManager = DBManager.getInstance();
    private TrainControl mTrainControl = TrainControl.getInstance();
    private ArrayList<String> mStationList = null;
    private ArrayList<Double> mStationMileageList = null;
    private ArrayList<Long> mStationScheduleTimeList = null;
    private long mWaitTime = 0;
    private int mCurrentStationIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_simpleschedule, container,false);
        initViews(view);
        initData();
        updateCurrentSchedule();
        return view;
    }

    private void updateCurrentSchedule() {
        mScheduleHandler.sendEmptyMessage(MsgConstant.MSG_UPDATE_CURRENT_STATION_NAME);
    }

    private void initData() {
        mStationList = mDBManager.getStationNames();
        mStationMileageList = mDBManager.getStationMileages();
        mStationScheduleTimeList =  mDBManager.getStationScheduleTimes();

        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_TRAIN_WAIT_TIME);
        filter.addAction(IntentConstants.ACTION_TRAIN_BEGIN_START);
        Myapp.sContext.registerReceiver(mScheduleReceiver,filter);
    }

    private void initViews(View view) {
        mTextPreStation = (TextView) view.findViewById(R.id.formerstation);
        mTextPreWaitTime = (TextView) view.findViewById(R.id.simple_schedule_text_pre_station_wait_time);
        mTextPreArriveTime =  (TextView) view.findViewById(R.id.formerstationarrivaltime);
        mTextPreMileage =  (TextView) view.findViewById(R.id.formerstationmileage);
        mTextNextWaitTime =  (TextView) view.findViewById(R.id.simple_schedule_text_current_station_wait_time);
        mTextNextStation =  (TextView) view.findViewById(R.id.simple_schedule_text_current_station_text);
        mTextNextArriveTime =  (TextView) view.findViewById(R.id.nextstationarrivaltime);
        mTextNextSchedule =  (TextView) view.findViewById(R.id.nextstationtrainplan);
        mTextNextMileage =  (TextView) view.findViewById(R.id.nextstationmileage);
    }

    private BroadcastReceiver mScheduleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_TRAIN_WAIT_TIME)){
                //靠站,停靠时间1秒钟记录一次
                mTrainControl.updateTrainWaitTime();
                mTextNextWaitTime.setText("2分钟");
            }else if(intent.getAction().equals(IntentConstants.ACTION_TRAIN_BEGIN_START)){
                //列车启动
                mScheduleHandler.sendEmptyMessage(MsgConstant.MSG_UPDATE_CURRENT_STATION_NAME);
                mTrainControl.setWaitTime(0);
            }
        }
    };

    private double currentMileage;
    private Handler mScheduleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_UPDATE_CURRENT_STATION_NAME:
                currentMileage = mTrainControl.getTotalMileage();

                if(currentMileage == 0 ){
                    //列车还未运行
                    mTextNextStation.setText(mStationList.get(1));
                    mTextNextMileage.setText("里程" + "\n" + Utils.convertDouble2Half(mStationMileageList.get(1)));
                    mTextNextSchedule.setText("当前计划" + "\n" +  "准时");
                    mTextNextArriveTime.setText("到达时间" + "\n" +
                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationScheduleTimeList.get(1))));
                    return;
                }

                if(currentMileage >= Utils.convertKM2M(mStationMileageList.get(mStationList.size()-1))){
                    mTextNextStation.setText(mStationList.get(mStationList.size()-1));
                    mTextNextMileage.setText("" + mStationMileageList.get(mStationMileageList.size()-1));
                    return;
                }

                for(int i=0;i<mStationList.size();i++){
                    if((i+1)==mStationList.size()){
                        break;
                    }

                    if(currentMileage <= Utils.convertKM2M(mStationMileageList.get(i+1))
                            &&
                            currentMileage>Utils.convertKM2M(mStationMileageList.get(i))){

                        mTextPreStation.setText(mStationList.get(i));
                        mTextPreMileage.setText("里程"  + mStationMileageList.get(i));
                        mTextPreArriveTime.setText("到达时间:" + mTrainControl.getArriveStationTime());

                        mTextNextStation.setText(mStationList.get(i+1));
                        mTextNextMileage.setText("里程" + "\n" +  mStationMileageList.get(i+1));
                        mTextNextSchedule.setText("当前计划" + "\n" +  "晚点");
                        mTextNextArriveTime.setText("到达时间" + "\n" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationScheduleTimeList.get(i+1))));

                        break;
                    }
                }

                break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Myapp.sContext.unregisterReceiver(mScheduleReceiver);

    }
}
