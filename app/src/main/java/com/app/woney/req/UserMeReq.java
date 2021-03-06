package com.app.woney.req;

import android.util.Log;

import com.app.woney.activity.MainActivity;
import com.app.woney.data.UserData;
import com.app.woney.data.WoneyKey;
import com.app.woney.fragment.EarnMainFragment;
import com.app.woney.util.RestClient;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/6.
 */

public class UserMeReq extends HttpReq {

    public UserMeReq(UserData userData) {
        super(WoneyKey.API_USE_ME, WoneyKey.NET_METHOD_GET, userData.getAccessHeaderMap());
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("HTTP", jsonObject.toString());
        UserData userData = MainActivity.getUser();

        if (jsonObject.length() != 0) {
            userData.updateWoneyDataByJson(jsonObject);
            userData.finishLoadWoney();

            MainActivity.setupWoneyCreditView();
            EarnMainFragment.setupBetsBtn();
        }
    }

    @Override
    public void onError() {
        // try signup again
        UserData userData = MainActivity.getUser();
        RestClient restClient = new RestClient(new SingUpReq(userData.getUserReq()));
        restClient.execute();
    }
}
