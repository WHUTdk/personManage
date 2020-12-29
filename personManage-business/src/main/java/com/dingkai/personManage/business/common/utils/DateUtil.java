package com.dingkai.personManage.business.common.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil extends DateUtils {

    /**
     * date类型转换为String类型
     *
     * @param data   Date类型时间
     * @param format 格式,如yyyy-MM-dd HH:mm:ss
     * @return string类型时间
     */
    public static String dateToString(Date data, String format) {
        return new SimpleDateFormat(format).format(data);
    }

    /**
     * @param strTime string类型时间
     * @param format  格式,如yyyy-MM-dd HH:mm:ss
     * @return Date类型时间
     */
    public static Date stringToDate(String strTime, String format)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(strTime);
    }

    /**
     * long转换为Date类型
     *
     * @param millSec long类型时间
     * @return Date类型时间
     */
    public static Date longToDate(long millSec) {
        return new Date(millSec);
    }

    /**
     * long类型转换为String类型
     *
     * @param millSec long类型时间
     * @param format  格式,如yyyy-MM-dd HH:mm:ss
     * @return string类型时间
     */
    public static String longToString(long millSec, String format) {
        Date date = longToDate(millSec); // long类型转成Date类型
        return dateToString(date, format);
    }

    /**
     * string类型转换为long类型
     *
     * @param strTime string类型时间
     * @param format  格式,如yyyy-MM-dd HH:mm:ss
     * @return long类型时间
     */
    public static long stringToLong(String strTime, String format)
            throws ParseException {
        Date date = stringToDate(strTime, format);
        if (date == null) {
            return 0;
        } else {
            return dateToLong(date);
        }
    }

    /**
     * string类型转换为string类型.
     *
     * @param strTime      string类型时间
     * @param sourceFormat 源时间格式,如yyyy-MM-dd
     * @param targetFormat 目标时间格式,如yyyyMMdd
     * @return string类型时间
     */
    public static String stringToString(String strTime, String sourceFormat, String targetFormat) throws ParseException {
        Date date = stringToDate(strTime, sourceFormat);
        return dateToString(date, targetFormat);
    }


    /**
     * date类型转换为long类型
     *
     * @param date Date类型时间
     * @return 日期毫秒值
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 获取当天零点的毫秒时间
     *
     * @return 零点的毫秒时间
     */
    public static long getTodayZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取目标天零点的毫秒时间
     *
     * @return 零点的毫秒时间
     */
    public static long getTargetDayZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据时间获取小时数
     *
     * @param date 时间
     * @return 小时数
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 根据年和月获取月的天数
     *
     * @param year  年
     * @param month 月
     * @return 月的天数
     */
    public static int getMonthDays(int year, int month) {
        int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = months[month - 1];
        //如果是2月并且是闰年,加一天
        if (month == 2 && (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)) {
            days++;
        }
        return days;
    }

    /**
     * 获取日期的小时差
     */
    public static double getHourDiff(Date date1, Date date2) {
        long date1Time = date1.getTime();
        long date2Time = date2.getTime();
        double hourDiff;
        if (date1Time > date2Time) {
            hourDiff = (double) (date1Time - date2Time) / (3600 * 1000);
        } else {
            hourDiff = (double) (date2Time - date1Time) / (3600 * 1000);
        }
        return hourDiff;
    }

    /**
     * 获取日期的分钟差
     */
    public static double getMinuteDiff(Date date1, Date date2) {
        long date1Time = date1.getTime();
        long date2Time = date2.getTime();
        double minuteDiff;
        if (date1Time > date2Time) {
            minuteDiff = (double) (date1Time - date2Time) / (60 * 1000);
        } else {
            minuteDiff = (double) (date2Time - date1Time) / (60 * 1000);
        }
        return minuteDiff;
    }

    /**
     * 给日期增加（减少）指定时间
     *
     * @param calendarField 时间类型,见CalendarField枚举类
     * @param amount        数量，可以为负数
     */
    public static Date add(Date date, int calendarField, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }


    public static void main(String[] args) throws ParseException {
        Date date = add(stringToDate("2020-08-01", "yyyy-MM-dd"), CalendarField.Day.getIndex(), -10);
        System.out.println(date);
        Date date1 = add(new Date(), CalendarField.Hour.getIndex(), -1);
        System.out.println(date1);
    }

    public enum CalendarField {
        Year(1),
        Month(2),
        Day(5),
        Hour(11),
        Minute(12),
        Second(13),
        Millisecond(14);

        public final int index;

        private CalendarField(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

}
