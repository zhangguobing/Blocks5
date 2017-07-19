package com.bing.blocks5.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * author：zhangguobing on 2017/7/18 15:03
 * email：bing901222@qq.com
 */

public class TimeUtil {


    /**
     * 获取当前日期
     * @return	yyyy-MM-dd
     */
    public static String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }


    /**
     * 获取当前日期是星期几
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取未来几天周几
     * @param interval
     * @return
     */
    public static List<String> getFutureWeeks(int interval){
        List<String> weekList = new ArrayList<>();
        for (int i = 0; i < interval; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + i);
            Date date = calendar.getTime();
            String week = getWeekOfDate(date);
            weekList.add(week);
        }
        return weekList;
    }

    /**
     * 获取未来几天的日期
     * @param interval 几天
     * @return pattern 日期格式
     */
    public static  List<String> getFutureDate(int interval, String pattern){
        List<String> futureDaysList = new ArrayList<>();
        for (int i = 0; i <interval; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + i);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            futureDaysList.add(format.format(date));
        }
        return futureDaysList;
    }


    /**
     * 获取某一天的开始时间
     * @return
     */
    public static String getStartTime(int amount) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(calendar.getTime()) + " 00:00:00";
    }

    /**
     * 获取某一天的结束时间
     * @return
     */
    public static String getEndTime(int amount) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(calendar.getTime()) + " 23:59:59";
    }
}
