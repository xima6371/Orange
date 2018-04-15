package com.xima.net.orange.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * *                 (c) Copyright 2018/4/9 by xima
 * *                          All Rights Reserved
 */
public class DateUtils {
    public static final String TYPE_NO_INTERVALS = "without intervals";

    public static final int MILLISECOND = 1000;
    public static final int MINUTE = 60 * MILLISECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static int getIntervals(Date date) {
        long intervals = -1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        try {
            long lastTime = dateFormat.parse(dateFormat.format(date)).getTime();
            long currentTime = System.currentTimeMillis();
            intervals = (currentTime - lastTime) / DAY;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(String.valueOf(intervals));
    }

    public static int[] getDayMonthYear(Date date) {
        int year;
        int month;
        int day;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        return new int[]{year, month, day};
    }

    /**
     * @param date event的date
     * @return 返回一个含有两个元素的int数组, 索引1是 day,索引0是 age
     */
    public static int[] getDaysBetweenBirthday(Date date) {
        int days = -1;
        int ages = -1;

        // 获取今天的年月日 举例2018-04-10
        int[] todayMsg = getDayMonthYear(new Date());

        int thisYear = todayMsg[0];
        int thisMonth = todayMsg[1];
        int today = todayMsg[2];

        //获取生日的年月日 举例1998-04-10
        int birthdayMsg[] = getDayMonthYear(date);
        int birthYear = birthdayMsg[0];
        int birthMonth = birthdayMsg[1];
        int birthday = birthdayMsg[2];


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //  ages = 2018-1998 = 20;
        ages = thisYear - birthYear;
        //  04 == 04
        if (thisMonth <= birthMonth) {
            ages -= 1;
            //获取今年生日
            calendar.set(Calendar.YEAR, thisYear);
            days = getIntervals(calendar.getTime());
        }


        if (thisMonth == birthMonth) {
            if (today < birthday) {
                ages -= 1;

                calendar.set(Calendar.YEAR, thisYear);
                days = getIntervals(calendar.getTime());
            } else {
                //今年生日已经过去超过0天，获取明年的生日间隔
                calendar.set(Calendar.YEAR, thisYear + 1);
                days = getIntervals(calendar.getTime());
            }
        }
        if (thisMonth > birthday) {
            //今年生日已经过去超过1个月，获取明年的生日间隔
            calendar.set(Calendar.YEAR, thisYear + 1);
            days = getIntervals(calendar.getTime());

        }

        return new int[]{ages, days};
    }


}
