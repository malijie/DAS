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
import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.manager.IntentManager;
import com.das.service.SimulatorService;
import com.das.util.Logger;
import com.das.util.Utils;
import com.example.das.R;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowEnergyFragment extends Fragment{
    private TrainControl mTrainControl = null;
    private TextView mTextTotalEnergy = null;
    private TextView mTextSuggestEnergy = null;
    private TextView mTextCurrentEnergy = null;
    private float mCurrentEnergy = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_energy, container,false);

        initViews(v);
        initData();

        calculateTotalEnergy();
        calculateCurrentEnergy();
        return v;
    }

    private void calculateCurrentEnergy() {

    }


    private void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_TOTAL_CONSUME_ENERGY);
        Myapp.sContext.registerReceiver(mEnergyReceiver,filter);

        mTrainControl = TrainControl.getInstance();
    }

    private void initViews(View v) {
        mTextTotalEnergy = (TextView) v.findViewById(R.id.id_energy_text_total_energy);
        mTextSuggestEnergy = (TextView) v.findViewById(R.id.id_energy_text_suggest_value);
        mTextCurrentEnergy = (TextView) v.findViewById(R.id.id_energy_text_current_value);

    }

    private void calculateTotalEnergy(){
        IntentManager.startService(SimulatorService.class,
                IntentConstants.ACTION_CALCULATE_TOTAL_CONSUME_ENERGY);
    }

    private Handler mEnergyHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_CALCULATE_CURRENT_ENERGY:
                    if(mTrainControl.getTrainCurrentStatus() == TrainConstants.TRAIN_RUN_STATUS_BREAK){
//                        mCurrentEnergy = mTrainControl.getCurrentSpeed() * TrainConstants.KM_PER_HOUR_2_M_PER_SECONDS *
                    }else if(mTrainControl.getTrainCurrentStatus() == TrainConstants.TRAIN_RUN_STATUS_ACCLERATE){

//                    }else{

                    }

//                    mTextCurrentEnergy.setText(status);
                    sendEmptyMessageDelayed(MsgConstant.MSG_CALCULATE_CURRENT_ENERGY,1000);
                    break;
            }
        }
    };

    private BroadcastReceiver mEnergyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("MLJ","intent=" + intent);
            if(intent.getAction().equals(IntentConstants.ACTION_UPDATE_TOTAL_CONSUME_ENERGY)){
                mTextTotalEnergy.setText(Utils.converDouble2Half(intent.getDoubleExtra("total_energy",0))
                                        + "KWH");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEnergyHandler.removeMessages(MsgConstant.MSG_CALCULATE_CURRENT_ENERGY);
        Myapp.sContext.unregisterReceiver(mEnergyReceiver);
    }
}
