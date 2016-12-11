package com.woney.req;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by houan on 2016/12/3.
 */

public abstract class HttpReq {

    private JSONObject body;
    private Map<String, String> header;
    private String apiPath;
    private String method;

    public abstract void onFinished(JSONObject jsonObject);

    public HttpReq(String apiPath, String method) {
        this(apiPath, method, new HashMap<String, String>(), new JSONObject());
    }

    public HttpReq(String apiPath, String method, Map<String, String> header) {
        this(apiPath, method, header, new JSONObject());
    }

    public HttpReq(String apiPath, String method, JSONObject body) {
        this(apiPath, method, new HashMap<String, String>(), body);
    }

    public HttpReq(String apiPath, String method, Map<String, String> header, JSONObject body) {
        this.apiPath = apiPath;
        this.method = method;
        this.header = header;
        this.body = body;
    }

    public final void onPreFinished(JSONObject jsonObject) {
        if (jsonObject.length() == 0) {
            Log.e("HttpReq", "Request failed!");
        } else {
            onFinished(jsonObject);
        }
    }

    public String getReqString() {
        if (body != null) {
            return body.toString();
        }
        return "";
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
