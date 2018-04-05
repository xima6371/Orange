package com.xima.net.orange.bean;

import android.content.Context;

import com.google.gson.Gson;

/**
 * *                 (c) Copyright 2018/4/5 by xima
 * *                          All Rights Reserved
 */
public class OrangeEvent {
    private Context mContext;
    private int mType;
    private String mPicturePath;
    private String mStartTime;
    private boolean mIsFavor;

    public OrangeEvent(Context context, int type, String picturePath, String startTime, boolean isFavor) {
        mContext = context;
        mType = type;
        mPicturePath = picturePath;
        mStartTime = startTime;
        mIsFavor = isFavor;
    }

    @Override
    public String toString() {
        //将Event对象转化成Json对象，方便读写
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
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

    public boolean isFavor() {
        return mIsFavor;
    }

    public void setFavor(boolean favor) {
        mIsFavor = favor;
    }
}
