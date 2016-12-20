package com.app.woney.req;

import android.util.Log;

import com.app.woney.activity.MainActivity;
import com.app.woney.data.UserData;
import com.app.woney.data.WoneyKey;
import com.app.woney.fragment.EarnMainFragment;
import com.app.woney.util.RestClient;

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

        Integer offlineGain = userData.getOfflineWoney();

        userData.updateWoneyDataByJson(jsonObject);
        userData.finishLoadWoney();

        EarnMainFragment.setupBetsBtn();

        if (offlineGain != null && offlineGain != 0) {
            UserGainReq gainReq = new UserGainReq(userData, offlineGain);
            RestClient restClient = new RestClient(gainReq);
            restClient.execute();

            userData.setOfflineWoney(0);
        } else {
            MainActivity.setupWoneyCreditView();
        }
    }
}
