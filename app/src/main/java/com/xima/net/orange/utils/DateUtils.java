package com.xima.net.orange.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * *                 (c) Copyright 2018/4/9 by xima
 * *                          All Rights Reserved
 */
public class DateUtils {

    public static final int MILLISECOND = 1000;
    public static final int MINUTE = 60 * MILLISECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * @param date             event的记录日期
     * @param todayIsAfterDate 今天的日期是否在event记录日期之后
     * @return
     */
    public static int getIntervals(Date date, boolean todayIsAfterDate) {

        int intervals;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        resetUnderDay(calendar);

        Date today = calendar.getTime();

        calendar.setTime(date);
        resetUnderDay(calendar);
        Date eventDay = calendar.getTime();

        if (todayIsAfterDate)
            intervals = (int) ((today.getTime() - eventDay.getTime()) / DAY);
        else
            intervals = (int) ((eventDay.getTime() - today.getTime()) / DAY);

//另一种实现方式,不过有版本限制 26以上版本才ok
//        int[] todayMsg = getYearMonthDay(new Date());
//        int[] dateMsg = getYearMonthDay(date);
//        intervals = ChronoUnit.DAYS.between(LocalDate.of(todayMsg[0],todayMsg[1],todayMsg[2]),LocalDate.of(dateMsg[0],dateMsg[1],dateMsg[2]));
        return intervals;
    }

    /**
     * 将天以下的计时单位:小时,分钟,秒,毫秒重置为0
     *
     * @param calendar 时间日历
     */
    public static void resetUnderDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static int[] getYearMonthDay(Date date) {
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
        int days = 0;
        int ages;

        // 获取今天的年月日 举例2018-04-10
        int[] todayMsg = getYearMonthDay(new Date());

        int thisYear = todayMsg[0];
        int thisMonth = todayMsg[1]+1;
        int today = todayMsg[2];

        //获取生日的年月日 举例1998-04-10
        int[] birthdayMsg = getYearMonthDay(date);
        int birthYear = birthdayMsg[0];
        int birthMonth = birthdayMsg[1];
        int birthday = birthdayMsg[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        ages = thisYear - birthYear;
        if (thisMonth < birthMonth) {
            //获取今年生日
            calendar.set(Calendar.YEAR, thisYear);
            days = getIntervals(calendar.getTime(), false);
        }

        if (thisMonth == birthMonth) {
            if (today < birthday) {
                calendar.set(Calendar.YEAR, thisYear);
                days = getIntervals(calendar.getTime(), true);
            } else if (today > birthday) {
                //今年生日已经过去超过x天，获取明年的生日间隔
                ages += 1;
                calendar.set(Calendar.YEAR, thisYear + 1);
                days = getIntervals(calendar.getTime(), true);
            } else {
                days = 0;
            }
        }

        if (thisMonth > birthMonth) {
            ages += 1;
            //今年生日已经过去超过1个月，获取明年的生日间隔
            calendar.set(Calendar.YEAR, thisYear + 1);
            days = getIntervals(calendar.getTime(), false);

        }
        return new int[]{ages, days};
    }


}
