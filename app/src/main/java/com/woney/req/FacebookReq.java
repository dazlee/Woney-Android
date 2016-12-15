package com.woney.req;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.woney.activity.MainActivity;
import com.woney.data.UserData;
import com.woney.fragment.EarnSettingFragment;
import com.woney.util.RestClient;

import org.json.JSONObject;

/**
 * Created by houan on 2016/12/3.
 */

public class FacebookReq {

    // TBD
    public static void loginFb(AccessToken accessToken) {
        GraphRequest meReq = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //讀出姓名 ID FB個人頁面連結
                        String id = object.optString("id");
                        String email = object.optString("email");
                        String gender = object.optString("gender");
                        Log.d("FB", "complete");
                        Log.d("FB", id);
                        Log.d("FB", email);
                        Log.d("FB", gender);

                        UserData userData = MainActivity.getUser();
                        userData.updateByReqCb(id, email, gender);

                        SingUpReq req = new SingUpReq(userData);
                        RestClient restClient = new RestClient(req);
                        restClient.execute();
                        userData.finishLoadFb();
                    }
                });

        //包入你想要得到的資料 送出request
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender");
        meReq.setParameters(parameters);
        meReq.executeAsync();
    }

    public static void sendMeReq(AccessToken accessToken) {
        GraphRequest meReq = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //讀出姓名 ID FB個人頁面連結
                        String id = object.optString("id");
                        String email = object.optString("email");
                        String gender = object.optString("gender");
                        Log.d("FB", "complete");
                        Log.d("FB", id);
                        Log.d("FB", email);
                        Log.d("FB", gender);

                        UserData userData = MainActivity.getUser();
                        userData.updateByReqCb(id, email, gender);

                        RestClient restClient = new RestClient(new UserMeReq(userData));
                        restClient.execute();

                        userData.finishLoadFb();
                        EarnSettingFragment.setupFbLoginView();
                    }
                });

        //包入你想要得到的資料 送出request
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender");
        meReq.setParameters(parameters);
        meReq.executeAsync();
    }
}
