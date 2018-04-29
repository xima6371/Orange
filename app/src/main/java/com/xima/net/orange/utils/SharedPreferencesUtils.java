package com.xima.net.orange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xima.net.orange.bean.OrangeEvent;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * *                 (c) Copyright 2018/4/14 by xima
 * *                          All Rights Reserved
 */
public class SharedPreferencesUtils {

    public static final String KEY_EVENTS = "key_events";

    public static void saveStringToSP(Context context, String key, String eventJson) {
        SharedPreferences sp = context.getSharedPreferences("orangeEventData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, eventJson);
        editor.apply();
    }

    public static String getStringFromSP(Context context, String key, String defaultStr) {
        SharedPreferences sp = context.getSharedPreferences("orangeEventData", MODE_PRIVATE);
        return sp.getString(key, defaultStr);
    }

    public static List<OrangeEvent> getListFromSp(Context context, String key, String defaultStr) {
        String eventJson = SharedPreferencesUtils.getStringFromSP(context, key, defaultStr);
        return new Gson().fromJson(eventJson, new TypeToken<List<OrangeEvent>>() {
        }.getType());
    }


}
