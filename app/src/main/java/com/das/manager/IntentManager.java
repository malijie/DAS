package com.das.manager;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/26.
 */
public class IntentManager {
    public static void startActivity(Activity from,Class des){
        from.startActivity(new Intent(from,des));
    }
}
