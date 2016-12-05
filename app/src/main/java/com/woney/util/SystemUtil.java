package com.woney.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.woney.R;
import com.woney.data.UserData;

/**
 * Created by houan on 2016/11/20.
 */

public class SystemUtil {

    private static void saveStringValue(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static void saveBooleanValue(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean isFirstStarted(Context context) {
        String key = context.getResources().getString(R.string.sys_is_first);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, true);
    }

    public static void finishedTour(Context context) {
        String key = context.getResources().getString(R.string.sys_is_first);
        saveBooleanValue(context, key, false);
    }

    public static void saveUser(Context context, UserData user) {
        Log.d("save user", user.toString());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Resources res = context.getResources();

        for (String key : res.getStringArray(R.array.fb_user_vars)) {
            editor.putString(key, user.getBasicData(key));
        }

        editor.commit();
    }

    public static UserData loadUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        UserData user = new UserData(res);
        for (String key : res.getStringArray(R.array.fb_user_vars)) {
            user.setBasicData(key, prefs.getString(key, null));
        }

        return user;
    }
}
