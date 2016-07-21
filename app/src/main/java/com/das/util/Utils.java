package com.das.util;

/**
 * Created by n on malijie 2016/7/21.
 */
public class Utils {
    public static String converDouble2Half(double num){
      return new java.text.DecimalFormat("#.00").format(num);
    }

}
