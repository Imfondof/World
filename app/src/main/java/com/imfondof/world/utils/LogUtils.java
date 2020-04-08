package com.imfondof.world.utils;

import android.util.Log;

/**
 * Imfondof on 2020/4/8
 */
public class LogUtils {
    private static final String TAG = "LogUtils";

    public static boolean DEBUG = true;

    public static void d(String TAG, String MSG) {
        if (DEBUG) {
            Log.d(TAG, MSG);
        }
    }
}
