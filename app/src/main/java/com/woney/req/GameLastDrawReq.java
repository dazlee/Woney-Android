package com.woney.req;

import com.woney.data.LastDrawData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnWinnerFragment;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/5.
 */

public class GameLastDrawReq extends HttpReq {

    public GameLastDrawReq() {
        super(WoneyKey.API_GAME_LASTDRAW, WoneyKey.NET_METHOD_GET);
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        LastDrawData lastDrawData = new LastDrawData();
        lastDrawData.updateDataByJson(jsonObject);
        lastDrawData.setFirstWinnerByJson(jsonObject);
        lastDrawData.setCommonWinnersByJson(jsonObject);

        EarnWinnerFragment.setLastDrawData(lastDrawData);
        EarnWinnerFragment.setupLastDrawView();
    }
}
