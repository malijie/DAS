package com.das.constants;

/**
 * Created by malijie on 2016/7/16.
 */
public class IntentConstants {
    /**
     * 广播Action
     */
    //获取当前速度
    public static final String ACTION_UPDATE_CURRENT_SPEED = "ACTION_UPDATE_CURRENT_SPEED";
    //更新列车当前状态
    public static final String ACTION_UPDATE_CURRENT_TRAIN_STATUS = "ACTION_UPDATE_CURRENT_TRAIN_STATUS";
    //开始启动列车模拟
    public static final String ACTION_START_SIMULATE = "ACTION_START_SIMULATE";
    //列车未启动
    public static final String ACTION_GET_TRAIN_CURRENT_STATUS_STOP = "ACTION_GET_TRAIN_CURRENT_STATUS_STOP";
    //计算当前列车运行速度
    public static final String ACTION_CALCULATE_TRAIN_SPEED = "ACTION_CALCULATE_TRAIN_SPEED";
    //计算列车建议速度
    public static final String ACTION_CALCULATE_TRAIN_SUGGEST_SPEED = "ACTION_CALCULATE_TRAIN_SUGGEST_SPEED";
    //更新列车建议速度
    public static final String ACTION_UPDATE_TRAIN_SUGGEST_SPEED = "ACTION_UPDATE_TRAIN_SUGGEST_SPEED";
    //计算列车限制速度
    public static final String ACTION_CALCULATE_TRAIN_LIMIT_SPEED = "ACTION_CALCULATE_TRAIN_LIMIT_SPEED";
    //更新列车限制速度
    public static final String ACTION_UPDATE_TRAIN_LIMIT_SPEED = "ACTION_UPDATE_TRAIN_LIMIT_SPEED";

}
