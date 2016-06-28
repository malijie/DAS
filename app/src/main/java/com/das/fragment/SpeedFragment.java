package com.das.fragment;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.das.R;

/**
 * Created by �� on 2016/3/16.
 */
public class SpeedFragment extends Fragment {

    private SensorManager sensorManager;
    private TextView accelerometer;
    double ss = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_speed, container,false);

        accelerometer = (TextView) view.findViewById(R.id.advicespeed);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (sensorManager != null){
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float Xacc = event.values[0];
            float Yacc = event.values[1];
            float Zacc = event.values[2];
            double speed = ss + 0.2 * Xacc;

            accelerometer.setText(Xacc + "\n" + ss + "\n" + speed);

        //    Myapp myapp = ((Myapp)getActivity().getApplicationContext());
        //    myapp.setMyspeed((float)speed);

            ss = speed;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
