package com.uuc.common.core.utils;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tqf
 * @Description  时间格式校验
 * @Version 1.0
 * @since 2020-09-15 16:49
 */
public class IsLegalDate {
 
 
    public static void main(String[] args) {
        //1、验证 yyyy-MM-dd HH:mm:dd 格式的日期
        String date = "2020-01-25 12:36:45";
        System.out.println("date "+isLegalDate(date.length(),date,"yyyy-MM-dd HH:mm:ss"));
 
        //2、验证 yyyy-MM-dd 格式的日期
        String yearMonthday = "2020-01-01";
        System.out.println("yearMonthday: "+isLegalDate(yearMonthday.length(),yearMonthday,"yyyy-MM-dd"));
 
        //3、验证 yyyy-MM 格式的日期
        String yearMonth = "2020-02";
        System.out.println("yearMonth: "+isLegalDate(yearMonth.length(),yearMonth,"yyyy-MM"));
 
        //4、验证 yyyy 格式的日期
        String year = "2020";
        System.out.println("year: "+isLegalDate(year.length(),year,"yyyy"));
 
        //5、验证 HH:mm:ss 格式的日期
        String hms = "12:36:89";
        System.out.println("hms: "+isLegalDate(hms.length(),hms,"HH:mm:ss"));
 
 
 
 
    }
 
    /**
     * 根据时间 和时间格式 校验是否正确
     * @param length 校验的长度
     * @param sDate 校验的日期
     * @param format 校验的格式
     * @return
     */
    public static boolean isLegalDate(int length, String sDate,String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
}