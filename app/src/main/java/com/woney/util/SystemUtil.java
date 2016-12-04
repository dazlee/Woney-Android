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

        editor.putString(res.getString(R.string.user_facebook_id), user.getFacebookID());
        editor.putString(res.getString(R.string.user_first_name), user.getFirstName());
        editor.putString(res.getString(R.string.user_last_name), user.getLastName());
        editor.putString(res.getString(R.string.user_middle_name), user.getMiddleName());
        editor.putString(res.getString(R.string.user_email), user.getEmail());
        editor.putString(res.getString(R.string.user_gender), user.getGender());
        editor.putInt(res.getString(R.string.user_woney), user.getWoney());

        editor.commit();
    }

    public static UserData loadUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        UserData user = new UserData();
        user.setFacebookID(prefs.getString(res.getString(R.string.user_facebook_id), null));
        user.setFirstName(prefs.getString(res.getString(R.string.user_first_name), null));
        user.setLastName(prefs.getString(res.getString(R.string.user_last_name), null));
        user.setMiddleName(prefs.getString(res.getString(R.string.user_middle_name), null));
        user.setEmail(prefs.getString(res.getString(R.string.user_email), null));
        user.setGender(prefs.getString(res.getString(R.string.user_gender), null));
        user.setWoney(prefs.getInt(res.getString(R.string.user_woney), 0));

        return user;
    }
}
