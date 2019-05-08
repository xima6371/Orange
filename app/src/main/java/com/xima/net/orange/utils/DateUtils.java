package com.xima.net.orange.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * *                 (c) Copyright 2018/4/9 by xima
 * *                          All Rights Reserved
 */
public class DateUtils {


    public static int[] getYMD() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);//month范围是0-11,对应1-12月,
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new int[]{year, month, day};
    }


    public static int[] getDate(Date date) {
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


}
