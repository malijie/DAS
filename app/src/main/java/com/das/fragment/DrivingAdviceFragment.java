package com.das.fragment;

//import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.das.R;

import java.util.List;
import java.lang.Math;

/**
 * Created by �� on 2016/3/15.
 */
public class DrivingAdviceFragment extends Fragment {


    private TextView positionTextView;
    private TextView statusTextView;
    private String provider;

 /*   CalculateSpeedService.MyBinder binder;

    private ServiceConnection conn = new ServiceConnection(){
        @Override
        public void  onServiceConnected(ComponentName name, IBinder service)
        {
            System.out.println("Service Connected");
            binder = (CalculateSpeedService.MyBinder) service;
        }

        @Override
    public void onServiceDisconnected(ComponentName name)
        {
            System.out.println("Service Disconnected");
        }
    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivingadvice, container, false);
        positionTextView = (TextView) view.findViewById(R.id.currentcondition);
        statusTextView = (TextView) view.findViewById(R.id.nextcondition);

    /*  final Intent intent  = new Intent(getActivity(), CalculateSpeedService.class);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);

       String currentSpeed = "速度" + 3.6*binder.getSpeed();
        positionTextView.setText(currentSpeed);

        String currentGPSStatus = binder.getGPSStatus();
        statusTextView.setText(currentGPSStatus);*/

        return view;

    }



}
