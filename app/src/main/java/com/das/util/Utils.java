package com.das.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by n on malijie 2016/7/21.
 */
public class Utils {
    public static String convertDouble2Half(double num){
      return new java.text.DecimalFormat("#.00").format(num);
    }

    public static float convertFloat1Half(float num){
        BigDecimal b   =   new   BigDecimal(num);
        return b.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 千米转成米
     * @return
     */
    public static double convertKM2M(double km){
        return km * 1000;
    }

    /**
     * 千米转成米
     * @return
     */
    public static double convertM2kM(double m){
        return m / 1000;
    }


    /**
     * 秒转换为毫秒
     * @param second
     * @return
     */
    public static long second2Millis(long second){
        return second * 1000;
    }

    public static String millis2Time(long millis){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(millis);
    }

    public static float meterPerSecond2KMPerSecond(float meterPerSecond){
        return meterPerSecond * 3.6f;
    }


}
