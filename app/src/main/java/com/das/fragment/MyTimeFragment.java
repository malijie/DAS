package com.das.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.das.R;

/**
 * Created by 程 on 2016/3/24.
 */
public class MyTimeFragment extends Fragment {

    private static final int msgKey1 = 1;
    private TextView mTime;
    public String currenttime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.mytime, container,false);
        mTime = (TextView) view.findViewById(R.id.mytime);
        new TimeThread().start();
        return view;
    }


    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("  yyyy-MM-dd \n    HH:mm:ss  \n       E", sysTime);
                    mTime.setText(sysTimeStr);

                break;

                default:
                    break;
            }
        }
    };

}
