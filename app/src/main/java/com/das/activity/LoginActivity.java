package com.das.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.constants.Constants;
import com.das.db.DBManager;
import com.das.manager.IntentManager;
import com.das.manager.ToastManager;
import com.das.service.CalculateSpeedService;
import com.das.service.SimulatorService;
import com.das.util.DistanceUtil;
import com.das.util.SharePreferenceUtil;
import com.example.das.R;

/**
 * Created by Administrator on 2016/6/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView mTextConfirm = null;
    private TextView mTextTitle = null;
    private Spinner mSpinnerRouteInfo = null;
    private Spinner mSpinnerTrainInfo = null;
    private TrainControl mTrainControl = null;
    private DBManager mDBManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);

        initViews();
        initData();
        startServices();

    }

    private void initData() {

        mTrainControl = TrainControl.getInstance();
        mDBManager = DBManager.getInstance();

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

        mTextConfirm.setOnClickListener(this);
        mTextTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_login_text_confirm:

                break;
            case R.id.id_login_text_title:
//                if(checkPosition()){
//                    mDBManager.saveTheStartLatLng(mTrainControl.getCurrentLatitude(),
//                                                  mTrainControl.getCurrentLongitude());
                        SharePreferenceUtil.saveStartLatitude(123.0f);
                        SharePreferenceUtil.saveStartLongitude(234f);
                        IntentManager.startActivity(MainActivity.class);
//                }
                break;
            default:
                break;
        }
    }

    /**
     * 检测当前是否再离指定位置50米范围以内
     */
    private boolean checkPosition() {
        if(mTrainControl.getCurrentLatitude() == 0 && mTrainControl.getCurrentLongitude() ==0){
            ToastManager.showMsg("未完成定位，请稍后再试!");
            return false;
        }


        if(getStartDistance()> TrainConstants.DISTANCE_FROM_START_TO_CURRENT_LOCATION){
            ToastManager.showMsg("当前未在发车位置上，无法发车");
            return false;
        }

        return true;

    }

    private int getStartDistance(){
      return (int) DistanceUtil.getDistance(TrainConstants.RUTONG_START_LATITUDE,TrainConstants.RUTONG_START_LOGITUDE,
                mTrainControl.getCurrentLatitude(),mTrainControl.getCurrentLongitude()) * 1000;
    }

    private void startServices(){
        IntentManager.startService(CalculateSpeedService.class);
        IntentManager.startService(SimulatorService.class);
    }
}
