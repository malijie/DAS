package com.das.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.das.data.Constants;
import com.das.data.SingleTrainSimulator;
import com.das.file.FileManager;
import com.das.manager.IntentManager;
import com.example.das.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView mTextConfirm = null;
    private Spinner mSpinnerRouteInfo = null;
    private Spinner mSpinnerTrainInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        initViews();
        initData();
    }

    private void initData() {
        FileManager.getInstance().copyDBToRootDirectory();
        //数据
        SingleTrainSimulator.getInstance().updateTrainStatus();


        ArrayAdapter trainInfoAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.getTrainInfoList());;
        ArrayAdapter routeInfoAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.getRouteInfoList());;


        //设置样式
        trainInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //加载适配器
        mSpinnerRouteInfo.setAdapter(routeInfoAdapter);
        mSpinnerTrainInfo.setAdapter(trainInfoAdapter);
    }

    private void initViews() {
        mTextConfirm = (TextView) findViewById(R.id.id_login_text_confirm);
        mSpinnerRouteInfo = (Spinner) findViewById(R.id.id_login_spinner_route_info);
        mSpinnerTrainInfo = (Spinner) findViewById(R.id.id_login_spinner_train_info);

        mTextConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_login_text_confirm:
            IntentManager.startActivity(LoginActivity.this,MainActivity.class);
            break;
            default:
                break;
        }
    }
}
