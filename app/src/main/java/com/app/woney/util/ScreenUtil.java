package com.app.woney.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by houan on 2016/11/7.
 */

public class ScreenUtil {

    private static float scale = 0.0f;
    private static int deviceHeight;
    private static int deviceWidth;

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static float dp2px(Context context, float dpValue) {
        return Math.round(dpValue * getDisplayMetrics(context).density);
    }

    public static float px2dp(Context context, float pxValue) {
        return Math.round(pxValue / getDisplayMetrics(context).density);
    }

    public static float sp2px(Context context, float pxValue) {
        return Math.round(pxValue * getDisplayMetrics(context).scaledDensity);
    }

    public static float px2sp(Context context, float pxValue) {
        return Math.round(pxValue / getDisplayMetrics(context).scaledDensity);
    }

    public static int getScreenWidthPx(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeightPx(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static float getScreenWidthDp(Context context) {
        return Math.round(getScreenWidthPx(context) / getDisplayMetrics(context).density);
    }

    public static float getScreenHeightDp(Context context) {
        return Math.round(getScreenHeightPx(context) / getDisplayMetrics(context).density);
    }

    public static float getScale(Context context) {
        if (scale == 0.0f) {
            setScreenScale(context);
        }
        return scale;
    }

    public static void setScreenScale(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        deviceHeight = metrics.heightPixels;
        deviceWidth = metrics.widthPixels;

        scale = getScreenHeightPx(context)/deviceHeight;
    }
}
