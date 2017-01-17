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

    public SingUpReq(JSONObject reqJson) {
        super(WoneyKey.API_SINGUP, WoneyKey.NET_METHOD_POST, reqJson);
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        Log.d("HTTP", jsonObject.toString());
        UserData userData = MainActivity.getUser();

        Integer offlineGain = userData.getOfflineWoney();

        userData.updateWoneyDataByJson(jsonObject);
        userData.finishLoadWoney();

        EarnMainFragment.setupBetsBtn();

        if (offlineGain != null && offlineGain != 0) {
            UserGainReq gainReq = new UserGainReq(userData.getAccessHeaderMap(), offlineGain);
            RestClient restClient = new RestClient(gainReq);
            restClient.execute();

            userData.setOfflineWoney(0);
        } else {
            MainActivity.setupWoneyCreditView();
        }
    }
}
