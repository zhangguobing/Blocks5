package com.bing.blocks5.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
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
     */
    public static String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static long getTimeStamp(String time){
        try {
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            Date date = format.parse(time);
            return date.getTime();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
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


    /**
     * 聊天页面时间显示
     * @param time	标准格式时间（本地）
     * @param textView
     * @param timeOffset 时间偏差
     * @return
     */
    public static void displayTime(String time, TextView textView, double timeOffset){
        if(TextUtils.isEmpty(time)){
            return;
        }
        textView.setVisibility(View.VISIBLE);
        if(textView.getTag() == null || !textView.getTag().toString().equals(time)){
            try {
                SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                // 显示日期
                Date date_taget = defaultDateFormat.parse(time);
                // 12小时制
                Date date_taget_h = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault()).parse(time);

                // 显示日期时间零点，用来计算偏差天数
                Date date_taget_zero = defaultDateFormat.parse(time);
                date_taget_zero.setHours(0);
                date_taget_zero.setMinutes(0);
                date_taget_zero.setSeconds(0);

                // 当前日期零点，用来计算偏差天数
                Date date_taday_zero = new Date((long) (System.currentTimeMillis() + timeOffset * 1000));
                date_taday_zero.setHours(0);
                date_taday_zero.setMinutes(0);
                date_taday_zero.setSeconds(0);

                // 时区：早上、中午、下午。。。
                String timeBlock = getTimeblock(date_taget);
                // 偏差天数，用来计算目标日期是否是今天、昨天
                int dayOffset = (int) ((date_taday_zero.getTime() - date_taget_zero.getTime()) / (24 * 60 * 60 * 1000));

                if(date_taget.getYear() == date_taday_zero.getYear()){	// 同年
                    switch(dayOffset){
                        case 0:	// 今天
                            String hours = date_taget.getHours()==12?"12": String.valueOf(date_taget_h.getHours());	// 如果是中午12点的话 taget_h.getHours() 会是0，需要做一下判断。
                            textView.setText(timeBlock + " " + hours  + ":" + addZero(date_taget_h.getMinutes()));
                            break;
                        case 1:	// 昨天
                            textView.setText("昨天 " + date_taget.getHours() + ":" + addZero(date_taget.getMinutes()));
                            break;
                        default:	// 不是今天、昨天
                            if((date_taday_zero.getDay() != 0 && dayOffset < (date_taday_zero.getDay())) || (date_taday_zero.getDay() == 0 && dayOffset <= 6)){	// 同星期
                                textView.setText(getWeek(date_taget.getDay()) + " " + date_taget.getHours() + ":" + addZero(date_taget.getMinutes()));
                            }else{	// 不同星期
                                textView.setText(addZero(date_taget.getMonth()+1) + "-" + addZero(date_taget.getDate()) + " " + addZero(date_taget.getHours()) + ":" + addZero(date_taget.getMinutes()));
                            }
                    }
                }else{	// 不同年
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                    formatter.format(date_taget);
                }
                textView.setTag(time);

            } catch (ParseException e) {
                e.printStackTrace();
                textView.setText(time);
                textView.setTag(null);
            }
        }
    }


    private static String getWeek(int dayOfWeek){
        switch(dayOfWeek){
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 0:
                return "星期天";
            default:
                return "";
        }
    }

    /**
     * 如果传进来的是个位数，则在前面加个"0"再返回
     * @param d
     * @return
     */
    public static String addZero(int d){
        String result = String.valueOf(d);
        if(result.length() == 1){
            result = "0" + result;
        }
        return result;
    }


    /**
     * 获取时间区间   早上00-11  中午12  下午13-18  晚上19-24
     *
     * @return 时间区间字符串 "早上""中午""下午""晚上"
     */
    private static String getTimeblock(Date date){
        int i = date.getHours();
        String block = "";
        if(i >= 0 && i <= 11){
            block = "早上";
        }else if(i == 12){
            block = "中午";
        }else if(i >= 13 && i <= 18){
            block = "下午";
        }else if(i >= 19 && i <= 24){
            block = "晚上";
        }
        return block;
    }

}
