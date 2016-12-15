package com.woney.req;

import android.util.Log;

import com.woney.activity.MainActivity;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnMainFragment;
import com.woney.fragment.EarnSettingFragment;
import com.woney.util.RestClient;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/3.
 */

public class SingUpReq extends HttpReq {

    private UserData userData;

    public SingUpReq(UserData userData) {
        super(WoneyKey.API_SINGUP, WoneyKey.NET_METHOD_POST, userData.getUserReq());
        this.userData = userData;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("HTTP", jsonObject.toString());

        Integer localGain = userData.getWoney();

        userData.updateWoneyDataByJson(jsonObject);
        userData.finishLoadWoney();

        EarnMainFragment.setupBetsBtn();
        EarnSettingFragment.setupFbLoginView();

        if (localGain != null && localGain != 0) {
            UserGainReq gainReq = new UserGainReq(userData, localGain);
            RestClient restClient = new RestClient(gainReq);
            restClient.execute();
        } else {
            MainActivity.setupWoneyCreditView();
        }
    }
}
