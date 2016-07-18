package com.das.control;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/7/2.
 */
public class TrainConstants {
    //每隔3s查询速度
    public static final int TIME_INTERVAL = 3;
    // 米/秒转换成千米/小时
    public static final float KM_PER_HOUR_UNIT = 3.6f;
    //起始车站经度
    public static final double RUTONG_START_LATITUDE = 31.24002;
    //起始车站纬度
    public static final double RUTONG_START_LONGITUDE = 117.13971;
    //发车位置到当前位置距离
    public static final int DISTANCE_FROM_START_TO_CURRENT_LOCATION = 50;
    //牵引
    public static final int TRAIN_RUN_STATUS_ACCLERATE = 1;
    //制动
    public static final int TRAIN_RUN_STATUS_BREAK = -1;
    //惰行
    public static final int TRAIN_RUN_STATUS_KEEP = 0;
    //千米每小时转米每秒
    public static final double KM_PER_HOUR_2_M_PER_SECONDS = 0.2777778;

}
