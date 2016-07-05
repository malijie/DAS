package com.das.manager;

import android.widget.Toast;

import com.das.Myapp;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ToastManager {
    public static void showMsg(String msg){
        Toast.makeText(Myapp.sContext,msg,Toast.LENGTH_SHORT).show();
    }
}
