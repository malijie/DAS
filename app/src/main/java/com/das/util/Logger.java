package com.das.util;

import android.util.Log;

/**
 * Created by vic_ma on 16/26/6.
 * 日志类
 */
public class Logger {

    private static boolean DEBUG_MODE = false;

    public static void setLogSwitch(boolean isOpen){
        DEBUG_MODE = isOpen;
    }

    public static void d(String tag, String msg){
        if(true){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if(DEBUG_MODE){
            Log.i(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if(DEBUG_MODE){
            Log.e(tag,msg);
        }
    }

    public static void v(String tag, String msg){
        if(DEBUG_MODE){
            Log.v(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if(DEBUG_MODE){
            Log.w(tag,msg);
        }
    }

}
