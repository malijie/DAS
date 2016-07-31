package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.db.DBManager;
import com.das.util.Logger;
import com.example.das.R;

import java.util.ArrayList;

/**
 * Created by �� on 2016/3/16.
 */
public class SimpleScheduleFragment extends Fragment {
    private TextView mTextPreStation = null;
    private TextView mTextPreWaitTime = null;
    private TextView mTextPreArriveTime = null;
    private TextView mTextPreMileage = null;
    private TextView mTextCurWaitTime = null;
    private TextView mTextCurStation = null;
    private TextView mTextCurArriveTime = null;
    private TextView mTextCurSchedule = null;
    private TextView mTextCurMileage = null;

    private ArrayList<String> stationList = null;
    private DBManager mDBManager = DBManager.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_simpleschedule, container,false);
        initViews(view);
        initData();
        return view;
    }

    private void initData() {
        stationList = mDBManager.getStations();
        for(int i=0;i<stationList.size();i++){
            Logger.d("MLJ","stationList[i]" + i + "==" + stationList.get(i));
        }
    }

    private void initViews(View view) {
        mTextPreStation = (TextView) view.findViewById(R.id.formerstation);
        mTextPreWaitTime = (TextView) view.findViewById(R.id.simple_schedule_text_pre_station_wait_time);
        mTextPreArriveTime =  (TextView) view.findViewById(R.id.formerstationarrivaltime);
        mTextPreMileage =  (TextView) view.findViewById(R.id.formerstationmileage);
        mTextCurWaitTime =  (TextView) view.findViewById(R.id.simple_schedule_text_current_station_wait_time);
        mTextCurStation =  (TextView) view.findViewById(R.id.simple_schedule_text_current_station_text);
        mTextCurArriveTime =  (TextView) view.findViewById(R.id.nextstationarrivaltime);
        mTextCurSchedule =  (TextView) view.findViewById(R.id.nextstationtrainplan);
        mTextCurMileage =  (TextView) view.findViewById(R.id.nextstationmileage);
    }
}
