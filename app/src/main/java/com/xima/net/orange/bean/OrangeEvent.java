package com.xima.net.orange.bean;

import com.google.gson.Gson;

import java.util.Date;

/**
 * *                 (c) Copyright 2018/4/5 by xima
 * *                          All Rights Reserved
 */
public class OrangeEvent {
    public static final int TYPE_ANNIVERSARY = 2000;
    public static final int TYPE_BIRTHDAY = 2001;
    public static final int TYPE_COUNTDOWN = 2002;

    private String mTitle;
    private int mType;
    private String mPicturePath;
    private String mStartTime;
    private Date mDate;
    private boolean mIsTop;

    public OrangeEvent(String title, int type, String picturePath, String startTime, boolean isTop) {
        mTitle = title;
        mType = type;
        mPicturePath = picturePath;
        mStartTime = startTime;
        mIsTop = isTop;
    }

    public OrangeEvent(String title, int type, String picturePath, boolean isTop) {
        mTitle = title;
        mType = type;
        mPicturePath = picturePath;
        mIsTop = isTop;
    }

    @Override
    public String toString() {
        //将Event对象转化成Json对象，方便读写
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String picturePath) {
        mPicturePath = picturePath;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isTop() {
        return mIsTop;
    }

    public void setTop(boolean top) {
        mIsTop = top;
    }
}
