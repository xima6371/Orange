package com.xima.net.orange.bean;

import com.xima.net.orange.utils.Constant;
import com.xima.net.orange.utils.DateUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.Date;

public class Event extends LitePalSupport {

    @Column(defaultValue = Constant.EVENT_DEFAULT_PHOTO_PATH)
    private String path;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private String title;

    private boolean top;

    @Column(nullable = false)
    private Date date;

    @Column(unique = true)
    private long hash;//以修改日期作为id

    public long getHash() {
        return hash;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateDes() {
        int[] i = DateUtils.getDate(getDate());
        return (i[0] + "-" + (i[1] + 1) + "-" + i[2]);
    }

    public boolean isComing() {
        Date cur = new Date();
        Date last = getDate();

        //记录的月份和当前月份
        int lastMonth = getMonth(last);
        int curMonth = getMonth(cur);
        //记录的日子和今天
        int lastDay = getDay(last);
        int today = getDay(cur);

        return lastMonth == curMonth && lastDay == today;
    }

    public String getEventDes(boolean isWrap) {

        String wrap = isWrap ? "\n" : "";
        Date cur = new Date();
        Date last = getDate();

        //记录的月份和当前月份
        int lastMonth = getMonth(last);
        int curMonth = getMonth(cur);
        //记录的日子和今天
        int lastDay = getDay(last);
        int today = getDay(cur);

        switch (type) {
            case Constant.EVENT_TYPE_ANNIVERSARY:
                return "[" + getTitle() + "] " + wrap + "已经" + getIntervals(cur, last) + "天";

            case Constant.EVENT_TYPE_COUNTDOWN:
                if (getIntervals(last, cur) > 0) {
                    return "[" + getTitle() + "] " + wrap + "还有" + getIntervals(last, cur) + "天";
                }
                if (getIntervals(last, cur) < 0) {
                    return "[" + getTitle() + "] " + wrap + "反向倒数" + Math.abs(getIntervals(last, cur)) + "天";
                }
                return "[" + getTitle() + "] " + "倒数日已经到来";

            case Constant.EVENT_TYPE_BIRTHDAY:

                String title = getTitle().replace("生日", "");

                //获取年纪
                int age = getAge();
                //设置今年的生日日期
                Calendar c = Calendar.getInstance();
                c.setTime(last);
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + age);
                Date birthday = c.getTime();

                //还没出生的情况
                if (age < 0) {
                    return "[" + title + "]" + wrap + "还有" + Math.abs(getIntervals(cur, last)) + "天出生！";
                }
                //已出生的情况
                if (age > 0) {
                    //生日当天
                    if (isComing()) {
                        return "[" + title + "] " + age + "岁生日快乐";
                    }
                    //今年的生日已经过了
                    if (lastMonth < curMonth || (lastMonth == curMonth && lastDay < today)) {
                        //设置明年的生日
                        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                        Date nextBirthday = c.getTime();
                        return "[" + title + "] " + (age + 1) + "岁生日" + wrap + "还有" + getIntervals(nextBirthday, cur) + "天";
                    }
                    //还没到生日
                    return "[" + title + "] " + age + "岁生日" + wrap + "还有" + getIntervals(birthday, cur) + "天";
                }

                //age为0的情况
                if (lastMonth - curMonth > 0) {
                    //还没出生
                    return "[" + getTitle() + "]" + wrap + "还有" + Math.abs(getIntervals(cur, birthday)) + "天出生！";
                } else {

                    if (lastDay - today > 0) {
                        //还差几天出生
                        return "[" + getTitle() + "] " + wrap + "还有" + Math.abs(getIntervals(cur, birthday)) + "天出生！";
                    }

                    //今天出生
                    if (isComing()) {
                        return "[" + title + "] " + "出生啦~";
                    }
                    //已经出生好几天
                    return "[" + title + "] " + age + "岁生日" + wrap + "还有" + getIntervals(cur, birthday) + "天";
                }

        }
        return null;
    }

    public int getAge() {
        return getYear(new Date()) - getYear(getDate());
    }

    public int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);

    }

    public int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH);

    }

    public int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH);

    }

    //返回间隔天数
    public int getIntervals(Date cur, Date last) {
        long curTime = resetDate(cur);
        long lastTime = resetDate(last);

        return (int) ((curTime - lastTime) / (24 * 60 * 60 * 1000));
    }

    public String getIntervals() {
        int day;
        switch (getType()) {
            case Constant.EVENT_TYPE_ANNIVERSARY:
                day = getIntervals(new Date(), getDate());
                break;
            case Constant.EVENT_TYPE_BIRTHDAY:
                //设置今年的生日日期
                Calendar c = Calendar.getInstance();
                c.setTime(getDate());
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + getAge());
                Date birthday = c.getTime();
                Date today = new Date();
                //今年生日已经过去
                if (getMonth(birthday) < getMonth(today)
                        || (getMonth(birthday) == getMonth(today) && getDay(birthday) < getDay(today))) {
                    c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);//配置明年生日
                    day = getIntervals(c.getTime(), new Date());//
                } else {
                    day = getIntervals(new Date(), birthday);
                }
                break;
            case Constant.EVENT_TYPE_COUNTDOWN:
                day = getIntervals(getDate(), new Date());
                break;
            default:
                day = 0;
                break;
        }

        return String.valueOf(day);
    }

    public long resetDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);//当天时间

        //重置为0时0分0秒0毫秒
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //返回long类型的时间
        return calendar.getTime().getTime();
    }
}
