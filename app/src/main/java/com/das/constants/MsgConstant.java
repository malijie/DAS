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





}
