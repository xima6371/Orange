package com.xima.net.orange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static com.xima.net.orange.adapter.OrangeEventsAdapter.DEBUG_TAG;

/**
 * *                 (c) Copyright 2018/4/14 by xima
 * *                          All Rights Reserved
 */
public class SharedPreferencesUtils {

    public static final String KEY_EVENTS = "key_events";

    public static String getStringFromSP(Context context, String key, String defaultString) {
        SharedPreferences sp = context.getSharedPreferences("orangeEventData", MODE_PRIVATE);
        return sp.getString(key, defaultString);
    }

    public static void saveStringToSP(Context context, String key, String eventJson) {
        SharedPreferences sp = context.getSharedPreferences("orangeEventData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, eventJson);
        Log.i(DEBUG_TAG, "saveStringToSP: "+eventJson);
        editor.apply();
    }
}
