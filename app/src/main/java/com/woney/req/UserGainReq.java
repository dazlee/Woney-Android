package com.woney.req;

import android.util.Log;

import com.woney.activity.MainActivity;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by houan on 2016/12/6.
 */

public class UserGainReq extends HttpReq {

    private static int gainWoney;
    private static boolean isDailyEarn;

    public UserGainReq(UserData userData, Integer gainWoney) {
        this(userData, gainWoney, false);
    }

    public UserGainReq(UserData userData, Integer gainWoney, boolean isDailyEarn) {
        super(WoneyKey.API_USE_ME_GAIN, WoneyKey.NET_METHOD_POST,
                userData.getAccessHeaderMap(), getReqJson(gainWoney));
        this.gainWoney = gainWoney;
        this.isDailyEarn = isDailyEarn;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("GainReq", jsonObject.toString());
        UserData userData = MainActivity.getUser();
        userData.addWoney(gainWoney);
        MainActivity.setupWoneyCreditView();
    }

    public static JSONObject getReqJson(Integer gain) {
        UserData userData = MainActivity.getUser();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(WoneyKey.getWoneyKey(), userData.getWoney());
            jsonObject.put(WoneyKey.getAddedWoneyKey(), gain);
            jsonObject.put(WoneyKey.getIsDailyEarnKey(), String.valueOf(isDailyEarn));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
