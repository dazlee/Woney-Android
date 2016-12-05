package com.woney.req;

import android.content.ContentValues;
import android.util.Log;

import com.woney.data.UserData;
import com.woney.data.WoneyContent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by houan on 2016/12/3.
 */

public class SingUpReq extends HttpReq {

    public SingUpReq(UserData userData) {
        super(WoneyContent.API_SINGUP, WoneyContent.NET_METHOD_POST, userData);
    }

    @Override
    protected JSONObject genJsonReq(UserData userData) {
        JSONObject jsonObject = new JSONObject();

        ContentValues dataMap = userData.getBasicDataMap();
        try {
            for (String key : dataMap.keySet()) {
                jsonObject.put(key, dataMap.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void onFinished(String retString) {
        try {
            JSONObject jsonObject = new JSONObject(retString);
            Log.d("HTTP", jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
