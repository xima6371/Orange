package com.xima.net.orange.utils;

import android.util.Log;


public class LogUtils {
    private static final String DEBUG_TAG = "debug";

    public static void i(String des, String msg) {
        Log.i(DEBUG_TAG, des + ":" + msg);
    }
}
