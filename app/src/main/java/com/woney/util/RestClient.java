package com.woney.util;

import android.os.AsyncTask;
import android.util.Log;

import com.woney.req.HttpReq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by houan on 2016/12/1.
 */

public class RestClient extends AsyncTask<Void, Void, String> {
    private static String DEV_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";
    private static String PROD_URL = "http://ec2-54-250-151-131.ap-northeast-1.compute.amazonaws.com:3000";

    private HttpReq httpReq;

    private HttpURLConnection connection;

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

            if (reqString != null && !reqString.isEmpty()) {
                OutputStream os = connection.getOutputStream();
                os.write(httpReq.getReqString().getBytes());
                os.flush();
                os.close();
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String result = reader.readLine();
            //JSONObject jsonObj = new JSONObject(result);
            //Log.d("singup", "Result: " + jsonObj.getJSONObject("responseData").opt("translatedText"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        httpReq.onFinished(result);
    }

    private void initConn(String apiPath, String method) {
        try {
            URL url = new URL(DEV_URL + apiPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
            connection.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
