package com.das.control;

import android.os.Handler;
import android.os.Message;

import com.das.constants.MsgConstant;
import com.das.manager.BaiduLocationManager;
import com.das.constants.IntentConstants;
import com.das.manager.IntentManager;

/**
 * Created by malijie on 2016/7/2.
 */
public class TrainControl {
    private static final Object sObject = new Object();

    private static TrainControl mTrainControl = null;
    private BaiduLocationManager mLocationManager = null;

    private int mLastSpeed;
    private int mCurrentSpeed;
    private int mTrainCurrentRunningStatus;

    private TrainControl(){
        mLocationManager = BaiduLocationManager.getInstance();
    }

    public static TrainControl getInstance(){
        if (mTrainControl == null){
            synchronized (sObject){
                if(mTrainControl == null){
                    mTrainControl = new TrainControl();
                }
            }
        }
        return mTrainControl;
    }

    public int getCurrentSpeed(){
       return (int)mLocationManager.getCurrentSpeed();
    }


    private double rad(double d) {
        return (d * Math.PI / 180.0);
    }

    public double getCurrentLatitude(){
        return mLocationManager.getCurrentLatitude();
    }

    public double getCurrentLongitude() {
        return mLocationManager.getCurrentLongitude();
    }

    public int getTrainCurrentStatus(){
        return mTrainCurrentRunningStatus;
    }

    public Handler getHandler(){
        return mTrainControlHandler;
    }

    private Handler mTrainControlHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_GET_TRAIN_CURRENT_SPEED:
                    if(mLocationManager.getCurrentSpeed() == 0){
                        IntentManager.sendBroadcastMsg(IntentConstants.ACTION_GET_TRAIN_CURRENT_STATUS_STOP);
                        return;
                    }
                    mLastSpeed = (int) mLocationManager.getCurrentSpeed();
                    sendEmptyMessageDelayed(MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS,1000);
                    break;
                case MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS:
                    mCurrentSpeed = (int) mLocationManager.getCurrentSpeed();
                    if(mCurrentSpeed > mLastSpeed){
                        mTrainCurrentRunningStatus = TrainConstants.TRAIN_RUN_STATUS_ACCLERATE;
                    }else if(mCurrentSpeed == mLastSpeed){
                        mTrainCurrentRunningStatus = TrainConstants.TRAIN_RUN_STATUS_KEEP;
                    }else{
                        mTrainCurrentRunningStatus = TrainConstants.TRAIN_RUN_STATUS_BREAK;
                    }

                    IntentManager.sendBroadcastMsg(IntentConstants.ACTION_UPDATE_CURRENT_TRAIN_STATUS,
                            "train_running_status",mTrainCurrentRunningStatus);

                    sendEmptyMessage(MsgConstant.MSG_GET_TRAIN_CURRENT_SPEED);

                    break;
            }
        }
    };


}
