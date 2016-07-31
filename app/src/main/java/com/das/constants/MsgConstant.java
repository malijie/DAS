package com.das.constants;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MsgConstant {

    public static final int MSG_GET_TRAIN_CURRENT_STATUS = 0x0001;
    public static final int MSG_GET_TRAIN_CURRENT_SPEED = 0x0002;
    public static final int MSG_CALCULATE_SPEED = 0x0003;
    public static final int MSG_GET_LAST_SPEED_INFO = 0x0004;
    public static final int MSG_CALCULATE_SUGGEST_SPEED = 0x0005;
    public static final int MSG_CALCULATE_LIMIT_SPEED = 0x0006;
    public static final int MSG_CALCULATE_TOTAL_ENERGY = 0x0007;
    public static final int MSG_CALCULATE_TOTAL_MILEAGE = 0x0008;
    //更新运行曲线,建议速度
    public static final int MSG_UPDATE_RUNNING_CURVE_SUGGEST_SPEED = 0x0009;
    //更新运行曲线，限制速度
    public static final int MSG_UPDATE_RUNNING_CURVE_LIMIT_SPEED = 0x0010;
    //计算当前能耗
    public static final int MSG_CALCULATE_CURRENT_ENERGY = 0x0011;
    //计算当前已跑里程
    public static final int MSG_CALCULATE_CURRENT_TOTAL_MILEAGE = 0x0012;
    //更新当前站名
    public static final int MSG_UPDATE_CURRENT_STATION_NAME = 0X0013;
    //更新当前停靠时间
    public static final int MSG_UPDATE_CURRENT_WAIT_TIME = 0X0014;
    //更新当前到达时间
    public static final int MSG_UPDATE_CURRENT_ARRIVE_NAME = 0X0015;
    //更新当前计划
    public static final int MSG_UPDATE_CURRENT_SCHEDULE = 0X0016;
    //更新当前里程
    public static final int MSG_UPDATE_CURRENT_MILEAGE = 0X0017;
    //更新前一站名
    public static final int MSG_UPDATE_PRE_STATION_NAME = 0X0018;
    //更新前一站停靠时间
    public static final int MSG_UPDATE_PRE_STATION_WAIT_TIME = 0X0019;
    //更新前一站到达时间
    public static final int MSG_UPDATE_PRE_ARRIVE_TIME = 0X0020;
    //更新前一站里程
    public static final int MSG_UPDATE_PRE_MILEAGE = 0X0021;




}
