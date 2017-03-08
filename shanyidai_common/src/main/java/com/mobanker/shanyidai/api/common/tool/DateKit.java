package com.mobanker.shanyidai.api.common.tool;

import com.mobanker.shanyidai.api.common.enums.DatePattern;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date 相关处理工具类
 *
 * @author: R.Core
 * @date 创建时间：2016-12-16
 */
public class DateKit {



    /**
     * @param date
     * @param pattern
     * @return java.lang.String
     * @description 格式化时间
     * @author Richard Core
     * @time 2016/12/24 18:47
     * @method formatDate
     */
    public static String formatDate(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = DatePattern.DATE_TIME.getPattern();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * @param dateStr
     * @param pattern
     * @return java.util.Date
     * @description 转化时间字符串
     * @author Richard Core
     * @time 2016/12/24 18:53
     * @method parse2Date
     */
    public static Date parse2Date(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (StringUtils.isBlank(pattern)) {
            for (DatePattern datePattern : DatePattern.values()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern.getPattern());
                try {
                    Date date = simpleDateFormat.parse(dateStr);
                    return date;
                } catch (ParseException e) {
                    continue;
                }
            }
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = simpleDateFormat.parse(dateStr);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前时间戳
     *
     * @return
     * @author: liuyafei
     * @date 创建时间：2016年8月23日
     * @version 1.0
     * @parameter
     */
    public static Long getNowTime() {
        Date date = new Date();
        return date.getTime() / 1000;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long time) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time * 1000);
        ;
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws java.text.ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为日期
     */
    public static String dateToDay(String s) throws java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        return sdf.format(date);
    }

    /*
     * 将时间转换为日期
     */
    public static String dateToDayTime(String s) throws java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    /**
     * 获取当前倒推X秒的时间
     *
     * @param second
     * @return
     */
    public static Date getSubSecondDate(int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - second);
        return cal.getTime();
    }

    /**
     * 获取当前倒推X小时的时间[24小时制]
     *
     * @param hour
     * @return
     */
    public static Date getSubHourDate(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - hour);
        return cal.getTime();
    }
}
