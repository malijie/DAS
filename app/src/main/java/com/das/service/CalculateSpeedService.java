package com.das.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.das.constants.IntentConstants;
import com.das.constants.MsgConstant;
import com.das.control.TrainControl;
import com.das.manager.BaiduLocationManager;
import com.das.manager.IntentManager;
import com.das.util.Logger;

public class CalculateSpeedService extends Service {
    public static final String TAG = CalculateSpeedService.class.getSimpleName();
    private TrainControl mTrainControl = null;
    private BaiduLocationManager mLocationManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTrainControl = TrainControl.getInstance();
        mLocationManager = BaiduLocationManager.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
            String action = intent.getAction();
            Logger.d(TAG,"action=" + action);

            if(action.equals(IntentConstants.ACTION_CALCULATE_TRAIN_SPEED)){
                //计算当前速度
                mLocationManager.startLocation();
                mSpeedHandler.sendEmptyMessage(MsgConstant.MSG_GET_LAST_SPEED_INFO);
            }
        }

        return START_STICKY;
    }
    private static long mileage = 0;
    private Handler mSpeedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_GET_LAST_SPEED_INFO:
                    //获取最新速度
                    sendEmptyMessageDelayed(MsgConstant.MSG_CALCULATE_SPEED,1000);
                break;
                case MsgConstant.MSG_CALCULATE_SPEED:
                    //计算当前速度
                    IntentManager.sendBroadcastMsg(IntentConstants.ACTION_UPDATE_CURRENT_SPEED,
                            "speed",mTrainControl.getCurrentSpeed());
                    Logger.d(TAG,"MSG_CALCULATE_SPEED speed=" + mTrainControl.getCurrentSpeed());
                    sendEmptyMessage(MsgConstant.MSG_GET_LAST_SPEED_INFO);

                    mileage += 1000;
                    mTrainControl.setTotalMileage(mileage);

                    //到站
                    if(mTrainControl.getCurrentSpeed() == 0 && mTrainControl.getTotalMileage()>0){
                        IntentManager.sendBroadcastMsg(IntentConstants.ACTION_UPDATE_TRAIN_WAIT_TIME);
                    }

                    IntentManager.sendBroadcastMsg(IntentConstants.ACTION_TRAIN_BEGIN_START);


                break;
            }
        }
    };



    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpeedHandler.removeMessages( MsgConstant.MSG_GET_LAST_SPEED_INFO);
        mSpeedHandler.removeMessages( MsgConstant.MSG_CALCULATE_SPEED);
    }
}
