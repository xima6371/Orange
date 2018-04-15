package com.xima.net.orange.utils;

import android.content.Context;

import es.dmoral.toasty.Toasty;

/**
 * *                 (c) Copyright 2018/4/13 by xima
 * *                          All Rights Reserved
 */
public class ToastUtils {
    public static void error(Context context, String content) {
        Toasty.error(context, content).show();
    }

    public static void warning(Context context, String content) {
        Toasty.warning(context, content).show();
    }
}
