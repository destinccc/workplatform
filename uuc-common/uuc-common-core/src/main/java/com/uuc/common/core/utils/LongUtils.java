package com.uuc.common.core.utils;

/**
 * @author: fxm
 * @date: 2022-04-13
 * @description:
 **/
public class LongUtils {

    /**
     * Long数据判空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Long obj) {
        if (obj == null || obj == 0) {
            return true;
        }
        return false;
    }

    /**
     * Long数据非空判断
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Long obj) {
        return isEmpty(obj) ? true : false;
    }

    /**
     * Long数组判空
     *
     * @param objs
     * @return boolean
     */
    public static boolean isEmpty(Long[] objs) {
        if (objs != null && objs.length > 0) {
            return false;
        }
        return true;
    }

    /**
     * Long数组非空判断
     *
     * @param objs
     * @return boolean
     */
    public static boolean isNotEmpty(Long[] objs) {
        if (objs != null && objs.length > 0) {
            return true;
        }
        return false;
    }


    /**
     * Long数组转成String数组
     * @param objs
     * @return String[]
     */
    public static String[] caseLongArr2StringArr(Long[] objs) {
        if (isEmpty(objs)) {
            return null;
        }
        String[] arr = new String[objs.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = String.valueOf(objs[i]);
        }
        return arr;
    }
}
