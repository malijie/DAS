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

import com.das.Myapp;
import com.das.constants.IntentConstants;
import com.das.manager.IntentManager;
import com.das.service.SimulatorService;
import com.das.util.Logger;
import com.das.util.Utils;
import com.example.das.R;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowEnergyFragment extends Fragment{
    private TextView mTextTotalEnergy = null;
    private TextView mTextSuggestEnergy = null;
    private TextView mTextCurrentEnergy = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_energy, container,false);

        initViews(v);
        initData();

        calculateTotalEnergy();
        return v;
    }


    private void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_UPDATE_TOTAL_CONSUME_ENERGY);
        Myapp.sContext.registerReceiver(mEnergyReceiver,filter);
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
        Myapp.sContext.unregisterReceiver(mEnergyReceiver);
    }
}
