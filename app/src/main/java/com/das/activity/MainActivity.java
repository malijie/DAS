package com.das.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
//import android.support.v4.app.FragmentActivity;

import com.das.control.TrainControl;
import com.das.data.DataManager;
import com.das.db.DBManager;
import com.das.util.Logger;
import com.example.das.R;

/**
 * Created by �� on 2016/3/14.
 */
public class MainActivity extends Activity implements View.OnClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.layout_maininterface);

            Button buttonenergy = (Button) findViewById(R.id.button_energy);
            buttonenergy.setOnClickListener(this);
            Button buttonschedule = (Button) findViewById(R.id.button_schedule);
            buttonschedule.setOnClickListener(this);
            Button buttondrivingspeedcurves = (Button) findViewById(R.id.button_drivingspeedcurves);
            buttondrivingspeedcurves.setOnClickListener(this);
            Button buttontraininformation = (Button) findViewById(R.id.button_traininformation);
            buttontraininformation.setOnClickListener(this);
            Button buttonsettings = (Button) findViewById(R.id.button_settings);
            buttonsettings.setOnClickListener(this);
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_energy:
                    Intent intent_energy = new Intent(MainActivity.this, EnergyActivity.class);
                    startActivity(intent_energy);
                    break;
                case R.id.button_schedule:
                    Intent intent_schedule = new Intent(MainActivity.this, ScheduleActivity.class);
                    startActivity(intent_schedule);
                    break;
                case R.id.button_traininformation:
                    Intent intent_traininformation = new Intent(MainActivity.this, TrainInformationActivity.class);
                    startActivity(intent_traininformation);
                    break;
                case R.id.button_drivingspeedcurves:
//                    Intent intent = new Intent(MainActivity.this,RunningCurveActivity.class);
//                    startActivity(intent);
                    Logger.d("MLJ","speed=" + TrainControl.getInstance().getCurrentSpeed(30.69719088,104.00874601,30.6971987,104.0087506));
                    break;

            }

        }

}
