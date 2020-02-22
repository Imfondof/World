package com.imfondof.wechat.util;

import android.util.Log;

import com.imfondof.wechat.BuildConfig;

/**
 * 统一管理日志是否打印
 * （release就不会打印这些日志了）
 */
public class L {
    private static final String TAG = "Imfondof";
    private static boolean sDebug = BuildConfig.DEBUG;

    public static void d(String msg, Object... args) {
        if (sDebug) {
            return;
        }
        Log.d(TAG, String.format(msg, args));
    }
}
