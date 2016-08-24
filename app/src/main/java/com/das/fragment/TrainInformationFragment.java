package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.util.SharePreferenceUtil;
import com.das.util.Utils;
import com.example.das.R;

/**
 * Created by 程 on 2016/3/31.
 */
public class TrainInformationFragment extends Fragment{
    private TextView mTextDriverName = null;
    private TextView mTextTrainNo = null;
    private TextView mTextTrainStyle = null;
    private TextView mTextScheduleStartTime = null;
    private TextView mTextScheduleEndTime = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_traininformation, container,false);
        initViews(view);
        initData();
        return view;
    }

    private void initData() {
        mTextDriverName.setText("司机:" + SharePreferenceUtil.loadDriverName());
        mTextTrainNo.setText("车次:" + SharePreferenceUtil.loadTrainNo());
        mTextTrainStyle.setText("车型:" + SharePreferenceUtil.loadTrainModel());
        mTextScheduleStartTime.setText("计划发车时间:" + SharePreferenceUtil.loadTrainStartTime());
        mTextScheduleEndTime.setText("计划达到时间:" + Utils.millis2CurrentTime(Utils.getStartTimeMillions() + 8580 * 1000));
    }

    private void initViews(View view) {
        mTextDriverName = (TextView) view.findViewById(R.id.driver);
        mTextTrainNo =  (TextView) view.findViewById(R.id.trainnumber);
        mTextTrainStyle =  (TextView) view.findViewById(R.id.trainmodel);
        mTextScheduleStartTime =  (TextView) view.findViewById(R.id.departuretime);
        mTextScheduleEndTime =  (TextView) view.findViewById(R.id.terminaltime);
    }
}
