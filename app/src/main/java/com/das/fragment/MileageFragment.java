package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.das.chart.MileageBarChart;
import com.example.das.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MileageFragment extends Fragment {
    private BarChart mMileageBarchart = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mileage, container,false);
        initViews(view);
        initData();
        return view;
    }


    private void initData() {
        MileageBarChart mileageBarChart = new MileageBarChart();
        mileageBarChart.showBarChart(mMileageBarchart, mileageBarChart.getBarData());

    }

    private void initViews(View view) {
        mMileageBarchart = (BarChart) view.findViewById(R.id.id_mileage_barchar);

    }
}
