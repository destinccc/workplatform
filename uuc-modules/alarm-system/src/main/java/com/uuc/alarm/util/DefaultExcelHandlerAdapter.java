package com.uuc.alarm.util;

import com.uuc.common.core.utils.poi.ExcelHandlerAdapter;

/**
 * @description List转换
 * @author llb
 * @since 2022/8/5 11:10
 */
public class DefaultExcelHandlerAdapter implements ExcelHandlerAdapter {

    @Override
    public Object format(Object value, String[] args) {
        if ((value == null || "".equals(value.toString())) && args.length > 0) {
            String arg = args[0];
            if ("Integer".equals(arg) || "Long".equals(arg)) {
                return 0;
            } else if ("Double".equals(arg) || "Float".equals(arg)) {
                return 0.0;
            } else if ("Boolean".equals(arg)) {
                return "false";
            } else {
                return "-";
            }
        }
        return value;
    }
}
