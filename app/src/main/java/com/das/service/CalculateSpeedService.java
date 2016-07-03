package com.das.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.das.control.TrainControl;
import com.das.data.DASLocationManager;
import com.das.util.Logger;

public class CalculateSpeedService extends Service {
    private static final String ACTION_REQUEST_LOCATION_UPDATE = "ACTION_REQUEST_LOCATION_UPDATE";

    private static final int MSG_CALCULATE_SPEED = 1;
    private static final int MSG_GET_LAST_SPEED_INFO = 2;

    private TrainControl mTrainControl = null;
    private DASLocationManager mLocationManager = null;
    private double mLastLatitude;
    private double mLastLongtitude;
    private double mCurrentLongtitude;
    private double mCurrentLatitude;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTrainControl = TrainControl.getInstance();
        mLocationManager = DASLocationManager.getInstance();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REQUEST_LOCATION_UPDATE);
        registerReceiver(receiver,filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mLocationManager.startRequestLocationUpdates();
        mSpeedHandler.sendEmptyMessage(MSG_GET_LAST_SPEED_INFO);

        return START_STICKY;
    }

    private Handler mSpeedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_LAST_SPEED_INFO:
                    mLastLatitude = mLocationManager.getLatitude();
                    mLastLongtitude = mLocationManager.getLongitude();
                    sendEmptyMessageDelayed(MSG_CALCULATE_SPEED,3000);
                break;
                case MSG_CALCULATE_SPEED:

                    mCurrentLatitude = mLocationManager.getLatitude();
                    mCurrentLongtitude = mLocationManager.getLongitude();

                    Toast.makeText(CalculateSpeedService.this,"current speed=" +
                            mTrainControl.getCurrentSpeed(mLastLatitude,mLastLongtitude,mCurrentLatitude,mCurrentLongtitude),
                            Toast.LENGTH_SHORT).show();

                    sendEmptyMessage(MSG_GET_LAST_SPEED_INFO);
                    break;
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_REQUEST_LOCATION_UPDATE)){
                mSpeedHandler.sendEmptyMessage(MSG_GET_LAST_SPEED_INFO);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
