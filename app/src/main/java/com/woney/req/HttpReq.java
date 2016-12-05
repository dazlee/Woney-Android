package com.woney.req;

import com.woney.data.UserData;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/3.
 */

public abstract class HttpReq {

    private JSONObject reqJson;

    private String apiPath;

    private String method;

    protected abstract JSONObject genJsonReq(UserData userData);

    public abstract void onFinished(String retString);

    public HttpReq(String apiPath, String method, UserData userData) {
        if (userData != null) {
            this.reqJson = genJsonReq(userData);
        }
        this.apiPath = apiPath;
        this.method = method;
    }

    public String getReqString() {
        if (reqJson != null) {
            return reqJson.toString();
        }
        return "";
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getMethod() {
        return method;
    }
}
