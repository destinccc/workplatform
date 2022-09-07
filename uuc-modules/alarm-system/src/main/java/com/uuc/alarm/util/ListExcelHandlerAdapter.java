package com.uuc.alarm.util;

import com.uuc.common.core.utils.poi.ExcelHandlerAdapter;

import java.util.List;

/**
 * @description List转换
 * @author llb
 * @since 2022/8/5 11:10
 */
public class ListExcelHandlerAdapter implements ExcelHandlerAdapter {

    @Override
    public Object format(Object value, String[] args) {
        if (!(value instanceof List) || value.toString().length() <= 2) {
            return "-";
        }
        return value.toString().substring(1, value.toString().length() - 1);
    }
}
