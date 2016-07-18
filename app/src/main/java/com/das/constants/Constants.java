package com.das.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malijie on 2016/7/5.
 */
public class Constants {

    public static List<String> getTrainInfoList(){
        String[] strTrainInfo = new String[]{
                "东风4D"
        };
        List<String> trainInfos = new ArrayList<>();
        for(int i=0;i<strTrainInfo.length;i++){
            trainInfos.add(strTrainInfo[i]);
        }
        return trainInfos;
    }

    public static List<String> getRouteInfoList(){
        String[] strRouteInfo = new String[]{
                "庐铜铁路_下行"
        };
        List<String> routeInfos = new ArrayList<>();
        for(int i=0;i<strRouteInfo.length;i++){
            routeInfos.add(strRouteInfo[i]);
        }
        return routeInfos;
    }

}
