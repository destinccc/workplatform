package com.uuc.common.core.utils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author uuc
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String HH_MM_SS = "HH:mm:ss";

    //    public static String CST_TIME = "EEE MMM dd HH:mm:ss 'CST' yyyy";
    public static String CST_TIME = "EEE MMM dd HH:mm:ss zzz yyyy";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static int getDistanceOfTwoDate(Date startTime, Date endTime)  {
        int i = differentDays(startTime, endTime);
//        long beforeTime = startTime.getTime();
//        long afterTime = endTime.getTime();
//        int dayNum = (int) (afterTime - beforeTime) / (1000 * 3600 * 24);
        System.out.println("开始日期:" + parseDateToStr(YYYY_MM_DD_HH_MM_SS, startTime) + " 结束日期: " + parseDateToStr(YYYY_MM_DD_HH_MM_SS, endTime) + "相隔天数: " + i);
        return Math.abs(i);
    }


    /**
     * endDate比beginDate多的天数
     *
     * @param beginDate
     * @param date2
     * @return
     */
    public static int differentDays(Date beginDate, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beginDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            return day2 - day1;
        }
    }

    public static List<String> getLastDayStr(Integer recentDay) {
        List<String> data = Lists.newArrayList();
        if (recentDay.intValue() < 1) {
            return data;
        }
        Date now = getNowDate();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        for (Integer day = recentDay; day > 0; day--) {
            Date lastDay = getLastDay(now, day);
            data.add(sdf.format(lastDay));
        }
        return data;
    }

    public static String getKsYunUtcTime() {
        // 获取金山云签名必须是UTC格式并且时区少8个小时
        Date now = getNowDate();
        now = getNextHourByIntervalHour(now,-8);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df.format(now);
    }

    public static String getUnixDateStr(Long unixTime) {
        if (Objects.isNull(unixTime)){
            return "";
        }
        try {
            Date date = new Date(unixTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
        }
        return "";
    }


    public static String parseYYYYMMDD(Date date) {
        if (Objects.isNull(date)){
            return "";
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Long getCurrentTimeMillis(){
        return System.currentTimeMillis();
    }

    public static String getNextHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(calendar.getTime());
        return format;
    }

    public static String getLatestHourByIntervalDay(int dayInteval) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - dayInteval,
                calendar.get(Calendar.HOUR_OF_DAY) + 1, 0, 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(calendar.getTime());
        return format;
    }

    /* hourInteval 为正数 */
    public static Date getNextHourByIntervalHour(Date startTime, int hourInteval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hourInteval);
//        if (hourInteval > 23) {
//            int addDayNum = (int) hourInteval / 24;
//            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + addDayNum);
//        }
        return calendar.getTime();
    }

    public static Date getLastDay(Date startTime, int dayinterval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - dayinterval);
        return calendar.getTime();
    }

    public static final String parseCST(final String str,String formate) {
        try {
            return new SimpleDateFormat(formate).format(new SimpleDateFormat(CST_TIME, Locale.US).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                return new SimpleDateFormat(HH_MM_SS).format(new SimpleDateFormat(CST_TIME,Locale.US).parse(str));
            } catch (ParseException parseException) {
            }
        }
        return str;
    }

    public static String parse2OtherFormate(String hiredTime, String fromFormate, String targetFormate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fromFormate);
            SimpleDateFormat targetSdf = new SimpleDateFormat(targetFormate);
            hiredTime = targetSdf.format(sdf.parse(hiredTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hiredTime;
    }

    /**
     * 获取今天开始时间
     */
    private static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTimeInMillis();
    }

    /**
     * 获取今天结束时间
     */
    private static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    public static Long getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis();
    }

    public static Long getEndDayOfYesterDay() {

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, -1);


        return cal.getTimeInMillis();
    }

    /**
     * 获取昨天开始时间和结束时间
     *
     * @return
     */
    public static String getYesterdayTime() {
        Long startTime = getBeginDayOfYesterday();
        Long endTime = getEndDayOfYesterDay();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        return startTimeStr;
    }
}
