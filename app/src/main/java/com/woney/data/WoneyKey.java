package com.woney.data;

import android.content.Context;
import android.content.res.Resources;

import com.woney.R;
import com.woney.activity.MainActivity;
import com.woney.fragment.EarnWinnerFragment;

/**
 * Created by houan on 2016/12/5.
 */

public class WoneyKey {

    private static Resources res;
    private static Context context;

    public static final boolean devMode = true;

    public static final String DEV_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";
    public static final String PROD_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";

    public static final String FB_FAN_PAGE = "https://www.facebook.com/woneyglobe";
    public static final String FB_FAN_PAGE_ID = "176487632824904";

    public static final String fbUrlFront = "https://graph.facebook.com/";
    public static final String fbUrlEnd = "/picture?type=large";

    public static final String NET_METHOD_GET = "GET";
    public static final String NET_METHOD_POST = "POST";

    public static final String API_SINGUP = "/api/signup";
    public static final String API_USE_ME = "/api/user/me";
    public static final String API_USE_ME_GAIN = "/api/user/me/gain";
    public static final String API_USE_ME_BET = "/api/user/me/bet";
    public static final String API_GAME_ONGOING = "/api/games/ongoing";
    public static final String API_GAME_LASTDRAW = "/api/games/lastDraw";

    public static final int SEC_WINNERS_COUNT = 10;
    public static final int EARN_DAILY = 20;
    public static final int EARN_FB_SHARE = 50;
    public static final int EARN_WATCH_AD = 50;
    public static final int woneyPerBets = 10;
    public static final int betsPerClick = 1;
    public static final int intervalShareHR = 12;
    public static final int lockWinnerSeries = 1;
    public static final int winnerDelayAdMillsec = 2000;

    public static final String GAIN_KEY = "gainKey";
    public static final String TEXT_KEY = "SHOW_KEY";

    public static void initRes(Context context) {
        WoneyKey.context = context;
        WoneyKey.res = context.getResources();
    }

    public static Context getContext() {
        return context;
    }

    public static String[] getStringArray(int index) {
        return res.getStringArray(index);
    }

    public static String getStringFormated(int index, Object... formatArgs) {
        return res.getString(index, formatArgs);
    }

    public static String[] getWoneyDataKeyArray() {
        return getStringArray(R.array.woney_user_vars);
    }

    public static String[] getFbKeyArray() {
        return getStringArray(R.array.fb_user_vars);
    }

    public static String[] getAccessKeyArray() {
        return getStringArray(R.array.api_header_access);
    }

    public static String getUserAccessKey() {
        return res.getString(R.string.user_access_token);
    }

    public static String getIsDailyEarnKey() {
        return res.getString(R.string.api_is_daily_earn);
    }

    public static String getIsFbShare() {
        return res.getString(R.string.api_is_fb_share);
    }

    public static String getFacebookIDKey() {
        return res.getString(R.string.fb_user_facebook_id);
    }

    public static String getFirstNameKey() {
        return res.getString(R.string.fb_user_first_name);
    }

    public static String getLastNameKey() {
        return res.getString(R.string.fb_user_last_name);
    }

    public static String getMiddleNameKey() {
        return res.getString(R.string.fb_user_middle_name);
    }

    public static String getDisplayNameKey() {
        return res.getString(R.string.fb_user_display_name);
    }

    public static String getNameKey() {
        return res.getString(R.string.fb_user_name);
    }

    public static String getEmailKey() {
        return res.getString(R.string.fb_user_email);
    }

    public static String getGenderKey() {
        return res.getString(R.string.fb_user_gender);
    }

    public static String getPhotoUrlKey() {
        return res.getString(R.string.fb_user_photo_url);
    }

    public static String getWoneyKey() {
        return res.getString(R.string.user_woney);
    }

    public static String getOfflineWoneyKey() {
        return res.getString(R.string.user_offline_woney);
    }

    public static String getTotalWoneyKey() {
        return res.getString(R.string.user_total_woney);
    }

    public static String getBetsKey() {
        return res.getString(R.string.user_bets);
    }

    public static String getLastDailyEarnKey() {
        return res.getString(R.string.woney_last_daily_earn);
    }

    public static String getLastFbShareKey() {
        return res.getString(R.string.woney_last_fb_share);
    }

    public static String getSeriesKey() {
        return res.getString(R.string.api_series);
    }

    public static String getAddedWoneyKey() {
        return res.getString(R.string.woney_added_woneys);
    }

    public static String getRewardKey() {
        return res.getString(R.string.api_reward);
    }

    public static String getSecRewardKey() {
        return res.getString(R.string.api_sec_reward);
    }

    public static String getEndTimeKey() {
        return res.getString(R.string.api_end_time);
    }

    public static String getIDKey() {
        return res.getString(R.string.api_id);
    }

    public static String getGameIdKey() {
        return res.getString(R.string.api_game_id);
    }

    public static String getFirstWinnerKey() {
        return res.getString(R.string.api_first_winner);
    }

    public static String getCommonWinnersKey() {
        return res.getString(R.string.api_common_winners);
    }

    public static Runnable delayAdShow = new Runnable() {
        @Override
        public void run() {
            if (MainActivity.getCurrentPosition() == 1) {
                MainActivity.requestNewInterstitial();
                EarnWinnerFragment.setAlreadyShowAd(true);
            }
        }
    };

    public static String loadFBPictureUrl(String facebookID) {
        return fbUrlFront + facebookID + fbUrlEnd;
    }
}
