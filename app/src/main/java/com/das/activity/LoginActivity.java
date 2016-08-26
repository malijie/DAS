package com.das.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.das.constants.IntentConstants;
import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.constants.Constants;
import com.das.db.DBManager;
import com.das.manager.BaiduLocationManager;
import com.das.manager.IntentManager;
import com.das.manager.ToastManager;
import com.das.service.CalculateSpeedService;
import com.das.service.SimulatorService;
import com.das.util.DistanceUtil;
import com.das.util.Logger;
import com.das.util.SharePreferenceUtil;
import com.das.util.Utils;
import com.example.das.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText mEditDriverName = null;
    private EditText mEditTrainNo = null;
    private TextView mTextConfirm = null;
    private TextView mTextTitle = null;
    private Spinner mSpinnerRouteInfo = null;
    private Spinner mSpinnerTrainInfo = null;

    private TrainControl mTrainControl = null;
    private DBManager mDBManager = null;
    private BaiduLocationManager mLocationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);

        initViews();
        initData();
        //开始监听当前速度

        IntentManager.startService(CalculateSpeedService.class,
                IntentConstants.ACTION_CALCULATE_TRAIN_SPEED);
        IntentManager.startService(SimulatorService.class,
                IntentConstants.ACTION_START_SIMULATE);

    }

    private void initData() {

        mTrainControl = TrainControl.getInstance();
        mDBManager = DBManager.getInstance();
        mLocationManager = BaiduLocationManager.getInstance();

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
        mTextTitle = (TextView) findViewById(R.id.id_login_text_title);
        mEditDriverName = (EditText) findViewById(R.id.id_login_edit_driver_name);
        mEditTrainNo = (EditText) findViewById(R.id.id_login_edit_train_no);

        mTextConfirm.setOnClickListener(this);
        mTextTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_login_text_confirm:
                if(checkPosition()) {
                    saveTrainInfo();
                    IntentManager.startActivity(MainActivity.class);

                }
                break;
            case R.id.id_login_text_title:
                if(checkPosition()) {
                    saveTrainInfo();
                    IntentManager.startActivity(MainActivity.class);
//                    IntentManager.startService(SimulatorService.class,
//                            IntentCo`
//
//
// ads
//
// asd
// as
// d
// asnstants.ACTION_START_SIMULATE);
                }
                break;
            default:
                break;
        }
    }

    private void saveTrainInfo(){
        SharePreferenceUtil.saveStartLatitude((float)TrainConstants.RUTONG_START_LATITUDE);
        SharePreferenceUtil.saveStartLongitude((float)TrainConstants.RUTONG_START_LONGITUDE);
        SharePreferenceUtil.saveDriverName(mEditDriverName.getText().toString());
        SharePreferenceUtil.saveTrainNo(mEditTrainNo.getText().toString());
        SharePreferenceUtil.saveTrainModel(mSpinnerTrainInfo.getSelectedItem().toString());
        SharePreferenceUtil.saveTrainStartTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        Utils.setStartTimeMillions(System.currentTimeMillis());

    }

    /**
     * 检测当前是否再离指定位置50米范围以内
     */
    private boolean checkPosition() {
        if(mTrainControl.getCurrentLatitude() == 0 && mTrainControl.getCurrentLongitude() ==0){
            Logger.d("MLJ","lat=" + mTrainControl.getCurrentLatitude() + ",lon=" + mTrainControl.getCurrentLongitude());
            ToastManager.showMsg("未完成定位，请稍后再试!");
            mLocationManager.startLocation();
            return false;
        }

//        if(getStartDistance()> TrainConstants.DISTANCE_FROM_START_TO_CURRENT_LOCATION){
//            ToastManager.showMsg("当前未在发车位置上，无法发车");
//            return false;
//        }

//        if(TextUtils.isEmpty(mEditDriverName.getText().toString())){
//            ToastManager.showMsg("列车司机不能为空");
//            return true;
//        }
//
//        if(TextUtils.isEmpty(mEditTrainNo.getText().toString())){
//            ToastManager.showMsg("车次不能为空");
//            return true;
//        }

        return true;
    }

    private int getStartDistance(){
      return (int) DistanceUtil.getDistance(TrainConstants.RUTONG_START_LATITUDE,TrainConstants.RUTONG_START_LONGITUDE,
                mTrainControl.getCurrentLatitude(),mTrainControl.getCurrentLongitude()) * 1000;
    }

}
