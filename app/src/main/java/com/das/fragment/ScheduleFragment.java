package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.control.TrainControl;
import com.das.db.DBManager;
import com.das.util.Logger;
import com.das.util.Utils;
import com.example.das.R;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> mStationNames = new ArrayList<>();
    private List<Integer> mStationWaitTimes = new ArrayList<>();
    private List<Double> mStationMileage = new ArrayList<>();
    private List<Long> mStationSchduleTimes = new ArrayList<>();

    private TrainControl mTrainControl = TrainControl.getInstance();
    private DBManager mDBManager = DBManager.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_schedule, container,false);
        initViews(view);
        initData();
        return view;
    }

    private int index = 1;

    private void initData() {
        mStationMileage = mDBManager.getStationMileages();
        mStationNames = mDBManager.getStationNames();
        mStationSchduleTimes = mDBManager.getStationScheduleTimes();
        mStationWaitTimes = mDBManager.getStationWaitTimes();

        double mileage = Utils.convertM2kM(mTrainControl.getTotalMileage());
        if(mileage <= mStationMileage.get(1)){
            //柯坦站与张旗杆站之间
            index = 1;
            mPreStationName.setText(mStationNames.get(index - 1));
            mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
            mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
            mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

            mNextStationName.setText(mStationNames.get(index));
            mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
            mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
            mNextStationMileage.setText("里程:" + mStationMileage.get(index));
            mNextStationPlan.setText("当前计划" + "\n" + "晚点");

            mNext2StationName.setText(mStationNames.get(index + 1));
            mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
            mNext2StationArriveTime.setText("到达时间:" +
                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                            + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
            mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

            mNext3StationName.setText(mStationNames.get(index + 2));
            mNext3StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 2) / 60 + "分钟");
            mNext3StationArriveTime.setText("到达时间:" +
                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                            + mStationSchduleTimes.get(index + 1)
                            + mStationSchduleTimes.get(index + 2))
                            + mStationWaitTimes.get(index)
                            + mStationWaitTimes.get(index + 1)));
            mNext3StationMileage.setText("里程:" + mStationMileage.get(index + 2));


            mNext4StationName.setText(mStationNames.get(index + 3));
            mNext4StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 3) / 60 + "分钟");
            mNext4StationArriveTime.setText("到达时间:" +
                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                            + mStationSchduleTimes.get(index + 1)
                            + mStationSchduleTimes.get(index + 2)
                            + mStationSchduleTimes.get(index + 3)
                            + mStationWaitTimes.get(index)
                            + mStationWaitTimes.get(index + 1)
                            + mStationWaitTimes.get(index + 2))));
            mNext4StationMileage.setText("里程:" + mStationMileage.get(index + 3));

        }else if(mileage > mStationMileage.get(1)&&mileage <= mStationMileage.get(2)){
                //张旗杆站与庐江南站之间
                index = 2;
                mPreStationName.setText(mStationNames.get(index - 1));
                mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
                mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
                mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

                mNextStationName.setText(mStationNames.get(index));
                mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
                mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
                mNextStationMileage.setText("里程:" + mStationMileage.get(index));
                mNextStationPlan.setText("当前计划" + "\n" + "晚点");

                mNext2StationName.setText(mStationNames.get(index + 1));
                mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
                mNext2StationArriveTime.setText("到达时间:" +
                        mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
                mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

                mNext3StationName.setText(mStationNames.get(index + 2));
                mNext3StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 2) / 60 + "分钟");
                mNext3StationArriveTime.setText("到达时间:" +
                        mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                + mStationSchduleTimes.get(index + 1)
                                + mStationSchduleTimes.get(index + 2))
                                + mStationWaitTimes.get(index)
                                + mStationWaitTimes.get(index + 1)));
                mNext3StationMileage.setText("里程:" + mStationMileage.get(index + 2));


                mNext4StationName.setText(mStationNames.get(index + 3));
                mNext4StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 3) / 60 + "分钟");
                mNext4StationArriveTime.setText("到达时间:" +
                        mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                + mStationSchduleTimes.get(index + 1)
                                + mStationSchduleTimes.get(index + 2)
                                + mStationSchduleTimes.get(index + 3)
                                + mStationWaitTimes.get(index)
                                + mStationWaitTimes.get(index + 1)
                                + mStationWaitTimes.get(index + 2))));
                mNext4StationMileage.setText("里程:" + mStationMileage.get(index + 3));
        }else if(mileage > mStationMileage.get(2) && mileage <= mStationMileage.get(3)){
                    //庐江南站与龙桥站之间
                    index = 3;
                    mPreStationName.setText(mStationNames.get(index - 1));
                    mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
                    mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
                    mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

                    mNextStationName.setText(mStationNames.get(index));
                    mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
                    mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
                    mNextStationMileage.setText("里程:" + mStationMileage.get(index));
                    mNextStationPlan.setText("当前计划" + "\n" + "晚点");

                    mNext2StationName.setText(mStationNames.get(index + 1));
                    mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
                    mNext2StationArriveTime.setText("到达时间:" +
                            mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                    + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
                    mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

                    mNext3StationName.setText(mStationNames.get(index + 2));
                    mNext3StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 2) / 60 + "分钟");
                    mNext3StationArriveTime.setText("到达时间:" +
                            mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                    + mStationSchduleTimes.get(index + 1)
                                    + mStationSchduleTimes.get(index + 2))
                                    + mStationWaitTimes.get(index)
                                    + mStationWaitTimes.get(index + 1)));
                    mNext3StationMileage.setText("里程:" + mStationMileage.get(index + 2));


                    mNext4StationName.setText(mStationNames.get(index + 3));
                    mNext4StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 3) / 60 + "分钟");
                    mNext4StationArriveTime.setText("到达时间:" +
                            mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                    + mStationSchduleTimes.get(index + 1)
                                    + mStationSchduleTimes.get(index + 2)
                                    + mStationSchduleTimes.get(index + 3)
                                    + mStationWaitTimes.get(index)
                                    + mStationWaitTimes.get(index + 1)
                                    + mStationWaitTimes.get(index + 2))));
                    mNext4StationMileage.setText("里程:" + mStationMileage.get(index + 3));
        }else if(mileage > mStationMileage.get(3) && mileage <= mStationMileage.get(4)){
                        //龙桥站与洪巷站之间
                        index = 4;
                        mPreStationName.setText(mStationNames.get(index - 1));
                        mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
                        mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
                        mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

                        mNextStationName.setText(mStationNames.get(index));
                        mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
                        mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
                        mNextStationMileage.setText("里程:" + mStationMileage.get(index));
                        mNextStationPlan.setText("当前计划" + "\n" + "晚点");

                        mNext2StationName.setText(mStationNames.get(index + 1));
                        mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
                        mNext2StationArriveTime.setText("到达时间:" +
                                mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                        + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
                        mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

                        mNext3StationName.setText(mStationNames.get(index + 2));
                        mNext3StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 2) / 60 + "分钟");
                        mNext3StationArriveTime.setText("到达时间:" +
                                mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                        + mStationSchduleTimes.get(index + 1)
                                        + mStationSchduleTimes.get(index + 2))
                                        + mStationWaitTimes.get(index)
                                        + mStationWaitTimes.get(index + 1)));
                        mNext3StationMileage.setText("里程:" + mStationMileage.get(index + 2));


                        mNext4StationName.setText(mStationNames.get(index + 3));
                        mNext4StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 3) / 60 + "分钟");
                        mNext4StationArriveTime.setText("到达时间:" +
                                mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                        + mStationSchduleTimes.get(index + 1)
                                        + mStationSchduleTimes.get(index + 2)
                                        + mStationSchduleTimes.get(index + 3)
                                        + mStationWaitTimes.get(index)
                                        + mStationWaitTimes.get(index + 1)
                                        + mStationWaitTimes.get(index + 2))));
                        mNext4StationMileage.setText("里程:" + mStationMileage.get(index + 3));
        }else if(mileage > mStationMileage.get(4) && mileage <= mStationMileage.get(5)){
                        //洪巷站与襄安站之间
                            index = 5;
                            mPreStationName.setText(mStationNames.get(index - 1));
                            mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
                            mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
                            mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

                            mNextStationName.setText(mStationNames.get(index));
                            mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
                            mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
                            mNextStationMileage.setText("里程:" + mStationMileage.get(index));
                            mNextStationPlan.setText("当前计划" + "\n" + "晚点");

                            mNext2StationName.setText(mStationNames.get(index + 1));
                            mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
                            mNext2StationArriveTime.setText("到达时间:" +
                                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                            + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
                            mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

                            mNext3StationName.setText(mStationNames.get(index + 2));
                            mNext3StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index + 2) / 60 + "分钟");
                            mNext3StationArriveTime.setText("到达时间:" +
                                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                                            + mStationSchduleTimes.get(index + 1)
                                            + mStationSchduleTimes.get(index + 2))
                                            + mStationWaitTimes.get(index)
                                            + mStationWaitTimes.get(index + 1)));
                            mNext3StationMileage.setText("里程:" + mStationMileage.get(index + 2));


                            mNext4StationName.setText("");
                            mNext4StationWaitTime.setText("停靠时间:");
                            mNext4StationArriveTime.setText("到达时间:");
                            mNext4StationMileage.setText("里程:");
        }else if(mileage > mStationMileage.get(5) && mileage <= mStationMileage.get(6)){
            //襄安站与无为南站之间
            index = 6;
            mPreStationName.setText(mStationNames.get(index - 1));
            mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index - 1) / 60 + "分钟");
            mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index - 1))));
            mPreStationMileage.setText("里程:" + mStationMileage.get(index - 1));

            mNextStationName.setText(mStationNames.get(index));
            mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index) / 60 + "分钟");
            mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
            mNextStationMileage.setText("里程:" + mStationMileage.get(index));
            mNextStationPlan.setText("当前计划" + "\n" + "晚点");

            mNext2StationName.setText(mStationNames.get(index + 1));
            mNext2StationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(2) / 60 + "分钟");
            mNext2StationArriveTime.setText("到达时间:" +
                    mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index)
                            + mStationSchduleTimes.get(index + 1) + mStationWaitTimes.get(index))));
            mNext2StationMileage.setText("里程:" + mStationMileage.get(index + 1));

            mNext3StationName.setText("");
            mNext3StationWaitTime.setText("停靠时间:");
            mNext3StationArriveTime.setText("到达时间:");
            mNext3StationMileage.setText("里程:");


            mNext4StationName.setText("");
            mNext4StationWaitTime.setText("停靠时间:");
            mNext4StationArriveTime.setText("到达时间:");
            mNext4StationMileage.setText("里程:");
        }else if(mileage > mStationMileage.get(6) && mileage <= mStationMileage.get(7)){
            //无为南站与上周村站
            index = 7;
            mPreStationName.setText(mStationNames.get(index -1));
            mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index -1)/60 + "分钟");
            mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index -1))));
            mPreStationMileage.setText("里程:" + mStationMileage.get(index -1));

            mNextStationName.setText(mStationNames.get(index));
            mNextStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index)/60 + "分钟");
            mNextStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index))));
            mNextStationMileage.setText("里程:" + mStationMileage.get(index));
            mNextStationPlan.setText("当前计划" + "\n" +"晚点");

            mNext2StationName.setText("");
            mNext2StationWaitTime.setText("停靠时间:");
            mNext2StationArriveTime.setText("到达时间:");
            mNext2StationMileage.setText("里程:");

            mNext3StationName.setText("");
            mNext3StationWaitTime.setText("停靠时间:");
            mNext3StationArriveTime.setText("到达时间:");
            mNext3StationMileage.setText("里程:");


            mNext4StationName.setText("");
            mNext4StationWaitTime.setText("停靠时间:" );
            mNext4StationArriveTime.setText("到达时间:");
            mNext4StationMileage.setText("里程:");
        }else if(mileage > mStationMileage.get(7)){
            //上周村站与钟鸣站
            index = 8;
            mPreStationName.setText(mStationNames.get(index -1));
            mPreStationWaitTime.setText("停靠时间:" + mStationWaitTimes.get(index -1)/60 + "分钟");
            mPreStationArriveTime.setText("到达时间:" + mTrainControl.getNextStationArriveTime(Utils.second2Millis(mStationSchduleTimes.get(index -1))));
            mPreStationMileage.setText("里程:" + mStationMileage.get(index -1));

            mNextStationName.setText("");
            mNextStationWaitTime.setText("停靠时间:");
            mNextStationArriveTime.setText("到达时间:");
            mNextStationMileage.setText("里程:" );
            mNextStationPlan.setText("当前计划" + "\n" +"晚点");

            mNext2StationName.setText("");
            mNext2StationWaitTime.setText("停靠时间:");
            mNext2StationArriveTime.setText("到达时间:");
            mNext2StationMileage.setText("里程:");

            mNext3StationName.setText("");
            mNext3StationWaitTime.setText("停靠时间:");
            mNext3StationArriveTime.setText("到达时间:");
            mNext3StationMileage.setText("里程:");


            mNext4StationName.setText("");
            mNext4StationWaitTime.setText("停靠时间:" );
            mNext4StationArriveTime.setText("到达时间:");
            mNext4StationMileage.setText("里程:");
        }
        index++;
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
