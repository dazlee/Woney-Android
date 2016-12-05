package com.woney.req;

import com.woney.data.UserData;
import com.woney.data.WoneyContent;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/5.
 */

public class GameLastDrawReq extends HttpReq {

    public GameLastDrawReq() {
        super(WoneyContent.API_GAME_LASTDRAW, WoneyContent.NET_METHOD_GET, null);
    }

    @Override
    protected JSONObject genJsonReq(UserData userData) {
        return null;
    }

    @Override
    public void onFinished(String retString) {

    }
}
