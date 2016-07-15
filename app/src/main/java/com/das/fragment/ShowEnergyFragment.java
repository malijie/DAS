package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.das.R;

/**
 * Created by �� on 2016/4/19.
 */
public class ShowEnergyFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_energy, container,false);

        initViews(v);
        initData();

        return v;
    }

    private void initData() {

    }

    private void initViews(View v) {
    }

}
