package com.das.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malijie on 2016/7/5.
 */
public class Constants {
    /**
     * 广播Action
     */
    //获取当前速度
    public static final String ACTION_UPDATE_CURRENT_SPEED = "ACTION_UPDATE_CURRENT_SPEED";


    public static List<String> getTrainInfoList(){
        String[] strTrainInfo = new String[]{
                "T1","T2","T3","T4"
        };
        List<String> trainInfos = new ArrayList<>();
        for(int i=0;i<strTrainInfo.length;i++){
            trainInfos.add(strTrainInfo[i]);
        }
        return trainInfos;
    }

    public static List<String> getRouteInfoList(){
        String[] strRouteInfo = new String[]{
                "北京-成都","成都-合肥","重庆-成都","北京-上海"
        };
        List<String> routeInfos = new ArrayList<>();
        for(int i=0;i<strRouteInfo.length;i++){
            routeInfos.add(strRouteInfo[i]);
        }
        return routeInfos;
    }

}
