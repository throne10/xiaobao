package com.xiaobao.good.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 简单时间工具
 */
public class DateUtil {

    /**
     * 一天的秒数
     */
    public static long ONE_DAY_MILLTIME = 24 * 60 * 60 * 1000;

    /**
     * @param date
     * @return
     */
    public static String DateToStringYMD(Date date) {
        return DateToString(date, "yyyy-MM-dd");
    }

    public static String DateToStringYMD2(Date date) {
        return DateToString(date, "yyyyMMdd");
    }

    public static String DateToStringHM(Date date) {
        return DateToString(date, "HH:mm");
    }

    public static String DateToStringYMDHM(Date date) {
        return DateToString(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * @param date
     * @return
     */
    public static String DateToString(Date date) {

        return DateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String DateToStringHMS(Date date) {
        return DateToString(date, "HH:mm:ss");
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static String DateToString(Date date, String format) {

        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            String sDate = sDateFormat.format(date);
            return sDate;
        } catch (Exception e) {
        }
        return null;
    }

    public static Date StringToDateYMD(String str) {
        return StringToDate(str, "yyyy-MM-dd");
    }

    public static Date StringToDate(String str, String format) {
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.getDefault());

            return sDateFormat.parse(str);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }
}
