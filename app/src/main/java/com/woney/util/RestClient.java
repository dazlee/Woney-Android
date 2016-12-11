package com.woney.util;

import android.os.AsyncTask;
import android.util.Log;

import com.woney.data.WoneyKey;
import com.woney.req.HttpReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by houan on 2016/12/1.
 */

public class RestClient extends AsyncTask<Void, Void, String> {
    private static String DEV_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";
    private static String PROD_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";

    private HttpReq httpReq;

    private HttpURLConnection connection;
    private int retCode;
    private String retMsg;

    public RestClient(HttpReq httpReq) {
        this.httpReq = httpReq;
    }

    @Override
    protected void onPreExecute() {
        initConn(httpReq.getApiPath(), httpReq.getMethod());
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String reqString = httpReq.getReqString();
            Log.d("HTTP", "Json req: " + reqString);

            connection.connect();

            if (reqString != null && !reqString.equals("{}")) {
                OutputStream os = connection.getOutputStream();
                os.write(httpReq.getReqString().getBytes());
                os.flush();
                os.close();
                retCode = connection.getResponseCode();
                retMsg = connection.getResponseMessage();
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Rest Api", "Response code: " + retCode);
            Log.e("Rest Api", "Error message: " + retMsg);
            return null;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        httpReq.onPreFinished(getJsonRet(result));
    }

    private void initConn(String apiPath, String method) {
        Map<String, String> headerMap = httpReq.getHeader();
        try {
            URL url = new URL(DEV_URL + apiPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            for (String key : headerMap.keySet()) {
                connection.addRequestProperty(key, headerMap.get(key));
            }

            if (method.equals(WoneyKey.NET_METHOD_POST)) {
                connection.setDoOutput(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getJsonRet(String retString) {
        JSONObject jsonObject = new JSONObject();
        if (retString != null) {
            try {
                jsonObject = new JSONObject(retString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
