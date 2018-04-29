package com.xima.net.orange.utils;

import com.xima.net.orange.bean.OrangeEvent;

import java.util.Calendar;
import java.util.Date;

import static com.xima.net.orange.bean.OrangeEvent.TYPE_ANNIVERSARY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_BIRTHDAY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_COUNTDOWN;
import static com.xima.net.orange.utils.DateUtils.DAY;

/**
 * *                 (c) Copyright 2018/4/14 by xima
 * *                          All Rights Reserved
 */
public class SwitchUtils {


    public static int getGap(OrangeEvent event) {

        int gap = 0;
        switch (event.getType()) {
            case TYPE_ANNIVERSARY:

                gap = DateUtils.getIntervals(event.getDate(), true);
                break;

            case TYPE_BIRTHDAY:
                gap = DateUtils.getDaysBetweenBirthday(event.getDate())[1];
                if (gap < 0)
                    gap = Math.abs(gap);
                break;

            case TYPE_COUNTDOWN:
                gap = DateUtils.getIntervals(event.getDate(), false);
                break;

            default:
                break;
        }
        return gap;
    }

    public static String[] getEventMsg(OrangeEvent event) {
        String eventDescription = "";
        String dateDescription = "";
        String title = event.getTitle();
        Date eventDate = event.getDate();


        switch (event.getType()) {

            case TYPE_ANNIVERSARY:
                int daysBetweenAnniversary = DateUtils.getIntervals(eventDate, true);

                if (daysBetweenAnniversary < 0) {
                    eventDescription = "你正在纪念未来" + Math.abs(daysBetweenAnniversary) + " 天的事：" + title;
                }
                if (daysBetweenAnniversary == 0) {
                    eventDescription = "今天是：" + title + " 纪念日哦！";
                }
                if (daysBetweenAnniversary > 0) {
                    eventDescription = title + "已纪念：" + daysBetweenAnniversary + " 天";
                }

                dateDescription = "纪念日: " + event.getStartTime();
                break;

            case TYPE_BIRTHDAY:
                int[] agesAndDays = DateUtils.getDaysBetweenBirthday(eventDate);
                int ages = agesAndDays[0];
                int days = agesAndDays[1];

                if (days < 0) {
                    days = Math.abs(days);
                }
                eventDescription = "距离" + title.replace("生日", "") + " " + ages + " 岁生日还有 " + days + " 天";
                if (days == 0) {
                    eventDescription = "今天" + title + ages + "岁生日噢,快送上生日祝福吧";
                }
                if (ages < 0 || (ages == 0 && agesAndDays[1] < 0)) {
                    eventDescription = "你的生日选错啦,请重选哦";
                }
                dateDescription = "生日: " + event.getStartTime();
                break;

            case TYPE_COUNTDOWN:
                int daysBetweenCount = DateUtils.getIntervals(eventDate, false);
                if (daysBetweenCount < 0) {
                    eventDescription = title + "反向记时已 " + Math.abs(daysBetweenCount) + " 天";
                }
                if (daysBetweenCount == 0) {
                    eventDescription = title + "已经到来";
                }
                if (daysBetweenCount > 0) {
                    eventDescription = title + "还有 " + daysBetweenCount + " 天";
                }
                dateDescription = "倒计时日: " + event.getStartTime();
                break;

            default:
                break;

        }
        return new String[]{eventDescription, dateDescription};
    }
}
