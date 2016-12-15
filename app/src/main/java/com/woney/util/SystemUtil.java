package com.woney.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.woney.R;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by houan on 2016/11/20.
 */

public class SystemUtil {

    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static boolean  isConnectedToInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showNoConnect(Context context) {
        Toast.makeText(context, context.getString(R.string.sys_no_connect), Toast.LENGTH_LONG).show();
    }

    public static void saveStringValue(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WoneyKey.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveIntegerValue(String key, Integer value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WoneyKey.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
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

    public static void saveUser(UserData user) {
        Log.d("save user", user.toString());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WoneyKey.getContext());
        SharedPreferences.Editor editor = prefs.edit();

        for (String key : WoneyKey.getFbKeyArray()) {
            editor.putString(key, user.getStringByKey(key));
        }

        for (String key : WoneyKey.getWoneyDataKeyArray()) {
            String value = user.getStringByKey(key);
            editor.putString(key, value);
        }

        editor.commit();
    }

    public static UserData loadUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WoneyKey.getContext());

        UserData user = new UserData();
        for (String key : WoneyKey.getFbKeyArray()) {
            user.setValuesByKey(key, prefs.getString(key, null));
        }

        for (String key : WoneyKey.getWoneyDataKeyArray()) {
            String value = prefs.getString(key, null);
            user.setValuesByKey(key, value);
        }

        return user;
    }

    public static Date tzStr2Date(String strDate) {
        try {
            return strDate != null ? dateFormat.parse(strDate) : null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String date2TzStr(Date date) {
        return dateFormat.format(date);
    }

    public static Bitmap loadPicture(String photoUrl) {
        Bitmap myPicture = null;
        try {
            URL profilePicUrl = new URL(photoUrl);
            myPicture = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream());
        } catch (Exception e) {
            Log.e("Photo", "Load picture failed! Url: " + photoUrl, e);
        }
        return myPicture;
    }

    public static String addZeroFront(int num) {
        return String.valueOf(num < 10 ? "0" + num : num);
    }

    public static String getFbLink(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + WoneyKey.FB_FAN_PAGE;
            } else { //older versions of fb app
                return "fb://page/" + WoneyKey.FB_FAN_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return WoneyKey.FB_FAN_PAGE; //normal web url
        }
    }
}
