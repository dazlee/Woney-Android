package com.woney.req;

import com.woney.activity.MainActivity;
import com.woney.data.OngoingData;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnMainFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by houan on 2016/12/6.
 */

public class UserBetReq extends HttpReq {

    public UserBetReq(UserData userData) {
        super(WoneyKey.API_USE_ME_BET, WoneyKey.NET_METHOD_POST,
                userData.getAccessHeaderMap(), getReqJson());
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        UserData userData = MainActivity.getUser();
        try {
            userData.setBets(jsonObject.getInt(WoneyKey.getBetsKey()));
            userData.setWoney(jsonObject.getInt(WoneyKey.getWoneyKey()));
            EarnMainFragment.setupBetsBtn();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getReqJson() {
        JSONObject jsonObject = new JSONObject();
        UserData userData = MainActivity.getUser();
        OngoingData ongoingData = EarnMainFragment.getOngoingData();

        try {
            jsonObject.put(WoneyKey.getGameIdKey(), ongoingData.getID());
            jsonObject.put(WoneyKey.getWoneyKey(), userData.getWoney());
            jsonObject.put(WoneyKey.getBetsKey(), userData.getBets());

            userData.clearSyncDrawTime();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
