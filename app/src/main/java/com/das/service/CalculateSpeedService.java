package com.das.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.das.control.TrainControl;
import com.das.manager.BaiduLocationManager;

public class CalculateSpeedService extends Service {
    private static final String ACTION_UPDATE_SPEED = "ACTION_UPDATE_SPEED";

    private static final int MSG_CALCULATE_SPEED = 1;
    private static final int MSG_GET_LAST_SPEED_INFO = 2;

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

        mLocationManager.start();
        mSpeedHandler.sendEmptyMessage(MSG_GET_LAST_SPEED_INFO);

        return START_STICKY;
    }

    private Handler mSpeedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_LAST_SPEED_INFO:
                    sendEmptyMessageDelayed(MSG_CALCULATE_SPEED,1000);
                break;
                case MSG_CALCULATE_SPEED:
                    sendUpdateSpeedMsg(mTrainControl.getCurrentSpeed());
                    sendEmptyMessage(MSG_GET_LAST_SPEED_INFO);
                    break;
            }
        }
    };

    private void sendUpdateSpeedMsg(int speed){
        Intent i = new Intent();
        i.setAction(ACTION_UPDATE_SPEED);
        i.putExtra("speed",speed);
        sendBroadcast(i);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
