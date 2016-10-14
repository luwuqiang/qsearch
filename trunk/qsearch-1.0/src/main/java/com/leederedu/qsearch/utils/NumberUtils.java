package com.leederedu.qsearch.utils;

/**
 * Created by liuwuqiang on 2016/10/14.
 */
public class NumberUtils {


    public static int toInt(Object val, int defaultVal) {
        if (val != null) {
            try {
                return Integer.parseInt(val.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return defaultVal;
    }

    public static long toLong(Object val, long defaultVal) {
        if (val != null) {
            try {
                return Long.parseLong(val.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return defaultVal;
    }


    public static float toFloat(Object val, float defaultVal) {
        if (val != null) {
            try {
                return Float.parseFloat(val.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return defaultVal;
    }

    public static double toDouble(Object val, double defaultVal) {
        if (val != null) {
            try {
                return Double.parseDouble(val.toString());
            } catch (NumberFormatException ex) {
            }
        }
        return defaultVal;
    }

}
