package com.xiaobao.good.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMPTY = "";
    public static final String UTF_8 = "UTF-8";
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private final static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        }
    };

    /**
     * 待验证的字符串是否为非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 待验证的字符串是否为空
     * 
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = true
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "".equals(str.trim());
    }

    /**
     * 待验证的字符串是否为数字型字符串
     * 
     * <pre>
     * StringUtil.isNumeric(null)   = false
     * StringUtil.isNumeric("")     = false
     * StringUtil.isNumeric("  ")   = false
     * StringUtil.isNumeric("123")  = true
     * StringUtil.isNumeric("12 3") = false
     * StringUtil.isNumeric("ab2c") = false
     * StringUtil.isNumeric("12-3") = false
     * StringUtil.isNumeric("12.3") = false
     * </pre>
     * @param value
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }

        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }

        return true;
    }

    /**
     * 布尔类型返回对应字符串值
     * @param value
     * @return
     */
    public static String booleanStr(Boolean value) {
        return Boolean.TRUE == value ? "Y" : "N";
    }

    /**
     * 字符串大写转换
     * @param str
     * @return
     */
    public static String toUpperCase(String str) {
        return isNotEmpty(str) ? str.toUpperCase(Locale.getDefault()) : EMPTY;
    }

    /**
     * 字符串小写转换
     * @param str
     * @return
     */
    public static String toLowerCase(String str) {
        return isNotEmpty(str) ? str.toLowerCase(Locale.getDefault()) : EMPTY;
    }

    /**
     * 格式化为日期时间
     * @param date
     * @return
     */
    public static String datetimeFormat(Date date) {
        return format(date, DATE_TIME_FORMAT);
    }

    /**
     * 转换成时间
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date paseDate(String date) throws ParseException {
        try {
            if (isEmpty(date)) {
                return null;
            }

            return sdf.get().parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 日期格式化
     * 
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }

    /**
     * 日期格式化
     * 
     * @param date
     * @param format
     * @return
     */

    public static boolean isTime(String time) {

        Pattern p = Pattern.compile("^[0-2][0-9]:[0-5][0-9]$");
        Matcher m = p.matcher(time.trim());

        if (m.matches()) {

            return true;
        }

        return false;
    }

    public static boolean isDateTime(String time) {
        Pattern p = Pattern.compile(".* \\d\\d:\\d\\d");
        Matcher m = p.matcher(time.trim());

        if (m.matches()) {

            return true;
        }
        return false;
    }

    public static String changeTime(String time) {

        return StringUtils.format(new Date(), "yyyy-MM-dd") + "-" + time;
    }
}
