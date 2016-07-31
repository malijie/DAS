package com.das.util;

/**
 * Created by n on malijie 2016/7/21.
 */
public class Utils {
    public static String convertDouble2Half(double num){
      return new java.text.DecimalFormat("#.00").format(num);
    }

    /**
     * 千米转成米
     * @return
     */
    public static double convertKM2M(double km){
        return km * 1000;
    }

    /**
     * 秒转换为毫秒
     * @param second
     * @return
     */
    public static long second2Millis(long second){
        return second * 1000;
    }

}
