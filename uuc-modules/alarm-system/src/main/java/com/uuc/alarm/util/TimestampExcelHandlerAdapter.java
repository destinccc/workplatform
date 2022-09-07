package com.uuc.alarm.util;

import com.uuc.common.core.utils.poi.ExcelHandlerAdapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @description 时间戳转换
 * @author llb
 * @since 2022/8/5 11:10
 */
public class TimestampExcelHandlerAdapter implements ExcelHandlerAdapter {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object format(Object value, String[] args) {
        if (!(value instanceof Long) || (Long) value == 0L) {
            return "-";
        }
        long longValue = ((Long) value);
        if (value.toString().length() == 10) {
            longValue = longValue * 1000L;
        }
        return DEFAULT_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault()));
    }
}
