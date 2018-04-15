package com.xima.net.orange.utils;

import com.xima.net.orange.bean.OrangeEvent;

import static com.xima.net.orange.bean.OrangeEvent.TYPE_ANNIVERSARY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_BIRTHDAY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_COUNTDOWN;

/**
 * *                 (c) Copyright 2018/4/14 by xima
 * *                          All Rights Reserved
 */
public class SwitchUtils {

    public static String[] getEventMsg(OrangeEvent event) {
        String eventDescription = "";
        String dateDescription = "";

        switch (event.getType()) {
            case TYPE_ANNIVERSARY:
                int daysBetweenAnniversary = DateUtils.getIntervals(event.getDate());

                eventDescription = event.getTitle() + "已经: " + daysBetweenAnniversary+" 天";
                if (daysBetweenAnniversary < 0) {
                    eventDescription = "日子都没到,纪念你个大头鬼";
                }

                dateDescription = "纪念日: " + event.getStartTime();

                break;

            case TYPE_BIRTHDAY:
                int[] agesAndDays = DateUtils.getDaysBetweenBirthday(event.getDate());
                int ages = agesAndDays[0];
                int days = agesAndDays[1];

                if (ages < 0 || days < 0) {
                    eventDescription = "你的生日选错啦";
                }

                if (days == 0) {
                    eventDescription = "今天它/他/她生日噢，快祝她生日快乐吧";
                } else {
                    eventDescription = "距离" + event.getTitle().replace("生日", "") +" "+ ages + " 生日还有 " + days + " 天";
                }

                dateDescription = "生日: " + event.getStartTime();

                break;

            case TYPE_COUNTDOWN:
                int daysBetweenCount = DateUtils.getIntervals(event.getDate());
                if (daysBetweenCount < 0) {
                    eventDescription = event.getTitle() + "反向记时已 " + Math.abs(daysBetweenCount) + " 天";
                }
                if (daysBetweenCount == 0) {
                    eventDescription = event.getTitle() + "已经到来";
                }

                if (daysBetweenCount > 0) {
                    eventDescription = event.getTitle() + "还有 " + daysBetweenCount + " 天到来";
                }
                dateDescription = "倒计时日: " + event.getStartTime();
                break;

            default:
                break;

        }
        return new String[]{eventDescription, dateDescription};
    }
}
