package com.das.control;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.das.constants.MsgConstant;
import com.das.manager.BaiduLocationManager;
import com.das.constants.IntentConstants;
import com.das.manager.IntentManager;
import com.das.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malijie on 2016/7/2.
 */
public class TrainControl {
    private static final Object sObject = new Object();

    private static TrainControl mTrainControl = null;
    private BaiduLocationManager mLocationManager = null;

    private int mTrainCurrentRunningStatus;
    private double mLimitSpeed;
    private double mSuggestSpeed;
    private ArrayList<Float> mCurrentSpeedList = new ArrayList<>();
    private ArrayList<Float> mLastSpeedList = new ArrayList<>();
    private static final int SPEED_LIST_SIZE = 3;
    private boolean mNeedPutInCurrentSpeedList = false;
    private boolean mNeedPutInLastSpeedList = true;


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


    public double getCurrentLatitude(){
        return mLocationManager.getCurrentLatitude();
    }

    public double getCurrentLongitude() {
        return mLocationManager.getCurrentLongitude();
    }



    public Handler getHandler(){
        return mTrainControlHandler;
    }

    private Handler mTrainControlHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_GET_TRAIN_CURRENT_SPEED:

//                    if(mLocationManager.getCurrentSpeed() == 0){
//                        IntentManager.sendBroadcastMsg(IntentConstants.ACTION_GET_TRAIN_CURRENT_STATUS_STOP);
//                        return;
//                    }
                    sendEmptyMessage(MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS);

                    break;
                case MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS:
                    float mSpeed = mLocationManager.getCurrentSpeed();
                    Logger.d("MLJ","speed=" + mSpeed);
                    if(mNeedPutInLastSpeedList && mLastSpeedList.size() != SPEED_LIST_SIZE){
                        mLastSpeedList.add(mSpeed);
                    }

                    if(mLastSpeedList.size() == SPEED_LIST_SIZE){
                        mNeedPutInLastSpeedList = false;
                        mNeedPutInCurrentSpeedList = true;
                    }

                    if(mNeedPutInCurrentSpeedList && mCurrentSpeedList.size() != SPEED_LIST_SIZE){
                        mCurrentSpeedList.add(mSpeed);
                    }

                    if(mCurrentSpeedList.size() == SPEED_LIST_SIZE){
                        mNeedPutInLastSpeedList = false;
                        mNeedPutInCurrentSpeedList = false;
                    }

                    if(!mNeedPutInLastSpeedList && !mNeedPutInCurrentSpeedList){
                        //集合满了，计算平均速度
                        int averLastSpeed = getAverageSpeed(mLastSpeedList);
                        int averCurrentSpeed = getAverageSpeed(mCurrentSpeedList);

                        Logger.d("MLJ","LastSpeedList=" + mLastSpeedList + ",CurrentSpeedList=" + mCurrentSpeedList
                                + "averLastSpeed=" + averLastSpeed + "averCurrentSpeed=" + averCurrentSpeed);
                        if(averCurrentSpeed > averLastSpeed){
                           setTrainCurrentStatus(TrainConstants.TRAIN_RUN_STATUS_ACCLERATE);
                        }else if(averCurrentSpeed == averLastSpeed){
                            setTrainCurrentStatus(TrainConstants.TRAIN_RUN_STATUS_KEEP);
                        }else{
                            setTrainCurrentStatus(TrainConstants.TRAIN_RUN_STATUS_BREAK);
                        }

                        IntentManager.sendBroadcastMsg(IntentConstants.ACTION_UPDATE_CURRENT_TRAIN_STATUS,
                            "train_running_status",getTrainCurrentStatus());

                        mNeedPutInLastSpeedList = true;
                        mNeedPutInCurrentSpeedList = false;
                        mCurrentSpeedList.clear();
                        mLastSpeedList.clear();
                    }

                    sendEmptyMessageDelayed(MsgConstant.MSG_GET_TRAIN_CURRENT_STATUS,500);

                    break;
            }
        }
    };

    private int getAverageSpeed(List<Float> speedList){
        int totalSpeed = 0;
        for(int i=0;i<speedList.size();i++){
            totalSpeed += speedList.get(i);
        }
        return totalSpeed/speedList.size();
    }

    public void setTrainCurrentStatus(int status){
        mTrainCurrentRunningStatus = status;
    }

    public int getTrainCurrentStatus(){
        return mTrainCurrentRunningStatus;
    }


    public void setSuggestSpeed(double suggestSpeed){
        this.mSuggestSpeed = suggestSpeed;
    }

    public double getSuggestSpeed(){
        return mSuggestSpeed;
    }

    public void setLimitSpeed(double limitSpeed){
        this.mLimitSpeed = limitSpeed;
    }

    public double getLimitSpeed(){
        return mLimitSpeed;
    }


}
