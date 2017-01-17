package com.app.woney.req;

import com.app.woney.activity.MainActivity;
import com.app.woney.data.OngoingData;
import com.app.woney.data.UserData;
import com.app.woney.data.WoneyKey;
import com.app.woney.fragment.EarnMainFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by houan on 2016/12/6.
 */

public class UserBetReq extends HttpReq {

    public UserBetReq(Map<String, String> accessMap) {
        super(WoneyKey.API_USE_ME_BET, WoneyKey.NET_METHOD_POST,
                accessMap, getReqJson());
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
        OngoingData ongoingData = OngoingData.getOngoingData();

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
