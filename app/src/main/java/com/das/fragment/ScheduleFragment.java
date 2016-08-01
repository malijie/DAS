package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.das.R;

/**
 * Created by 程 on 2016/3/30.
 */
public class ScheduleFragment extends Fragment {
    private TextView mPreStationName = null;
    private TextView mPreStationWaitTime = null;
    private TextView mPreStationArriveTime = null;
    private TextView mPreStationMileage = null;
    private TextView mNextStationName = null;
    private TextView mNextStationWaitTime = null;
    private TextView mNextStationArriveTime = null;
    private TextView mNextStationMileage = null;
    private TextView mNextStationPlan = null;
    private TextView mNext2StationName = null;
    private TextView mNext2StationWaitTime = null;
    private TextView mNext2StationArriveTime = null;
    private TextView mNext2StationMileage = null;
    private TextView mNext3StationName = null;
    private TextView mNext3StationWaitTime = null;
    private TextView mNext3StationArriveTime = null;
    private TextView mNext3StationMileage = null;
    private TextView mNext4StationName = null;
    private TextView mNext4StationWaitTime = null;
    private TextView mNext4StationArriveTime = null;
    private TextView mNext4StationMileage = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_schedule, container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mPreStationName = (TextView) view.findViewById(R.id.formerstation);
        mPreStationWaitTime = (TextView) view.findViewById(R.id.prewaittime);
        mPreStationArriveTime = (TextView) view.findViewById(R.id.formerstationarrivaltime);
        mPreStationMileage = (TextView) view.findViewById(R.id.formerstationmileage);
        mNextStationName = (TextView) view.findViewById(R.id.nextstation);
        mNextStationWaitTime = (TextView) view.findViewById(R.id.next_station_wait_time);
        mNextStationArriveTime = (TextView) view.findViewById(R.id.nextstationarrivaltime);
        mNextStationMileage = (TextView) view.findViewById(R.id.nextstationmileage);
        mNextStationPlan = (TextView) view.findViewById(R.id.nextstationtrainplan);
        mNext2StationName = (TextView) view.findViewById(R.id.secondstation);
        mNext2StationWaitTime = (TextView) view.findViewById(R.id.secondstationwaittime);
        mNext2StationArriveTime = (TextView) view.findViewById(R.id.secondstationarrivaltime);
        mNext2StationMileage = (TextView) view.findViewById(R.id.secondstationmileage);
        mNext3StationName = (TextView) view.findViewById(R.id.thirdstation);
        mNext3StationWaitTime = (TextView) view.findViewById(R.id.thirdstationwaittime);
        mNext3StationArriveTime = (TextView) view.findViewById(R.id.thirdstationarrivaltime);
        mNext3StationMileage = (TextView) view.findViewById(R.id.thirdstationmileage);
        mNext4StationName = (TextView) view.findViewById(R.id.fourthstation);
        mNext4StationWaitTime = (TextView) view.findViewById(R.id.fourthstationwaittime);
        mNext4StationArriveTime = (TextView) view.findViewById(R.id.fourthstationarrivaltime);
        mNext4StationMileage = (TextView) view.findViewById(R.id.fourthstationmileage);
    }
}
