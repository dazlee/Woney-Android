package com.woney.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.woney.R;

/**
 * Created by houan on 2016/11/20.
 */

public class SystemUtil {

    public static boolean isFirstStarted(Context context) {
        String key = context.getResources().getString(R.string.sys_is_first);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, true);
    }

    public static void finishedTour(Context context) {
        String key = context.getResources().getString(R.string.sys_is_first);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, Boolean.FALSE);
        editor.commit();
    }
}
