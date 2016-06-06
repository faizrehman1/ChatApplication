package com.example.faiz.mylogin.util;

import android.util.Log;

/**
 * Created by Kamran ALi on 5/30/2016.
 */
public class AppLogs {
    private static String TAG = "CHAT_APP";

    public static void logd(String msg) {
        Log.d(TAG, msg);
    }

    public static void loge(String msg) {
        Log.e(TAG, msg);
    }

    public static void logw(String msg) {
        Log.w(TAG, msg);
    }

    public static void logi(String msg) {
        Log.i(TAG, msg);
    }

}
