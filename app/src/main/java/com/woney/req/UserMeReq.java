package com.woney.req;

import android.util.Log;

import com.woney.activity.MainActivity;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnMainFragment;
import com.woney.util.RestClient;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/6.
 */

public class UserMeReq extends HttpReq {

    private UserData userData;

    public UserMeReq(UserData userData) {
        super(WoneyKey.API_USE_ME, WoneyKey.NET_METHOD_GET, userData.getAccessHeaderMap());
        this.userData = userData;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("HTTP", jsonObject.toString());

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
        RestClient restClient = new RestClient(new SingUpReq(userData));
        restClient.execute();
    }
}
