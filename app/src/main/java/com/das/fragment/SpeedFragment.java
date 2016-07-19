package com.das.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.constants.IntentConstants;
import com.das.control.TrainControl;
import com.das.manager.IntentManager;
import com.das.service.SimulatorService;
import com.example.das.R;

/**
 * Created by �� on 2016/3/16.
 */
public class SpeedFragment extends Fragment {
    private TextView mTextCurrentSpeed = null;
    private TextView mTextSuggestSpeed = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_speed, container,false);
        initViews(v);
        initData();
        calculateMileage();
        return v;
    }

    private void calculateMileage() {
        IntentManager.startService(SimulatorService.class,
                IntentConstants.ACTION_CALCULATE_TRAIN_SUGGEST_SPEED);

    }

    private void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_CURRENT_SPEED);
        filter.addAction(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED);
        this.getActivity().registerReceiver(mSpeedReceiver,filter);
        //计算里程
    }

    private void initViews(View v) {
        mTextSuggestSpeed = (TextView) v.findViewById(R.id.main_speed_text_suggest);
        mTextCurrentSpeed = (TextView) v.findViewById(R.id.currentspeed);
    }

    private BroadcastReceiver mSpeedReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_CURRENT_SPEED)){
                mTextCurrentSpeed.setText("当前速度:" + intent.getIntExtra("speed",0) + "KM/H"
                                    +"当前经纬度是:" + TrainControl.getInstance().getCurrentLatitude());
            }else if(action.equals(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED)){
                mTextSuggestSpeed.setText("建议速度:" + intent.getDoubleExtra("suggest_velocity",0) + "KM/H");
            }
        }
    };

    @Override
    public void onDestroy(){
        this.getActivity().unregisterReceiver(mSpeedReceiver);
        super.onDestroy();
    }

}
