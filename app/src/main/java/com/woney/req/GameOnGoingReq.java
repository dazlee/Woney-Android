package com.woney.req;

import com.woney.activity.MainActivity;
import com.woney.data.OngoingData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnMainFragment;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/6.
 */

public class GameOnGoingReq extends HttpReq {

    public GameOnGoingReq() {
        super(WoneyKey.API_GAME_ONGOING, WoneyKey.NET_METHOD_GET);
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        OngoingData ongoingData = new OngoingData();
        ongoingData.updateDataByJson(jsonObject);
        MainActivity.setViewPagerTouch(ongoingData);
        OngoingData.setOngoingData(ongoingData);
        EarnMainFragment.setupOngoingView();
    }
}
