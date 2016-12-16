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

    private int gainWoney;

    public UserGainReq(UserData userData, Integer gainWoney) {
        this(userData, gainWoney, false, false);
    }

    public UserGainReq(UserData userData, Integer gainWoney, boolean isDailyEarn, boolean isFbShare) {
        super(WoneyKey.API_USE_ME_GAIN, WoneyKey.NET_METHOD_POST,
                userData.getAccessHeaderMap(), getReqJson(gainWoney, isDailyEarn, isFbShare));
        this.gainWoney = gainWoney;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("GainReq", jsonObject.toString());
        UserData userData = MainActivity.getUser();

        try {
            Integer retWoney = jsonObject.getInt(WoneyKey.getWoneyKey());
            if (userData.getWoney() + gainWoney == retWoney) {
                userData.gainWoney(gainWoney);
            } else {
                Log.e("Gain", "Woney isn't match between local and response.");
            }

            MainActivity.setupWoneyCreditView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getReqJson(Integer gain, boolean isDailyEarn, boolean isFbShare) {
        UserData userData = MainActivity.getUser();
        int isDaily = isDailyEarn ? 1 : 0;
        int isFb = isFbShare ? 1 : 0;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(WoneyKey.getWoneyKey(), userData.getWoney() + gain);
            jsonObject.put(WoneyKey.getAddedWoneyKey(), gain);
            jsonObject.put(WoneyKey.getIsDailyEarnKey(), String.valueOf(isDaily));
            jsonObject.put(WoneyKey.getIsFbShare(), String.valueOf(isFb));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
