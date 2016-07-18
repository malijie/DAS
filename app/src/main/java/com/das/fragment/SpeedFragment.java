package com.das.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.constants.Constants;
import com.das.constants.IntentConstants;
import com.example.das.R;

/**
 * Created by �� on 2016/3/16.
 */
public class SpeedFragment extends Fragment {
    private TextView accelerometer;
    private TextView mTextCurrentSpeed = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_speed, container,false);
        initViews(v);
        initData();
        return v;
    }

    private void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_SPEED);
        this.getActivity().registerReceiver(mSpeedReceiver,filter);
    }

    private void initViews(View v) {
        accelerometer = (TextView) v.findViewById(R.id.advicespeed);
        mTextCurrentSpeed = (TextView) v.findViewById(R.id.currentspeed);
    }

    private BroadcastReceiver mSpeedReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_CURRENT_SPEED)){
                Log.d("MLJ","onReceive speed");
                mTextCurrentSpeed.setText("当前速度:" + intent.getIntExtra("speed",0) + "KM/H" +"当前经纬度是:" + intent.getDoubleExtra("lat",0));
            }
        }
    };

    @Override
    public void onDestroy(){
        this.getActivity().unregisterReceiver(mSpeedReceiver);
        super.onDestroy();
    }

}
