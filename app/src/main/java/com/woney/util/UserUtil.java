package com.woney.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.woney.activity.MainActivity;

import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by houan on 2016/11/13.
 */

public class UserUtil {
    private static final List<String> FB_GENERAL_PERMISSION = Arrays.asList("public_profile", "email");
    private static final int drawAtTime = 10;
    private static final int imgSize = 100;

    private static String firstName;
    private static String lastName;
    private static String middleName;
    private static String email;
    private static String gender;
    private static Bitmap myPicture = null;

    private static AccessToken accessToken;
    private static LoginManager loginManager;
    private static Profile profile;

    private static int myWoney = 2310;
    private static Date lastDailyEarn;

    public static void initUser() {
        loadFbProfile();
        if (UserUtil.isFbLogin()) {
            firstName = profile.getFirstName();
            lastName = profile.getLastName();
            middleName = profile.getMiddleName();
            myPicture = getProfilePicture();
        }
    }

    public static void loadFbProfile() {
        accessToken = AccessToken.getCurrentAccessToken();
        profile = Profile.getCurrentProfile();
    }

    public static boolean isEnoughDraw() {
        return (myWoney > drawAtTime) ? true : false;
    }

    public static boolean canEarnDaylyToday() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = formatter.parse(formatter.format(new Date()));

        if (lastDailyEarn == null || lastDailyEarn.before(today)) {
            lastDailyEarn = today;
            return true;
        }
        return false;
    }

    public static boolean isFbLogin() {
        return accessToken != null && profile != null ? true : false;
    }

    public static String getFacebookId() {
        return isFbLogin() ? profile.getId() : null;
    }

    public static List<String> getFbGeneralPermission() {
        return FB_GENERAL_PERMISSION;
    }

    public static void setMyPicture(Bitmap myPicture) {
        UserUtil.myPicture = myPicture;
    }

    public static String getFbName() {
        return (profile != null) ? profile.getName() : null;
    }

    public static Bitmap getProfilePicture() {
        if (myPicture == null) {
            try {
                URL profilePicUrl = new URL("http://graph.facebook.com/" + profile.getId() + "/picture?type=large");
                myPicture = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("FB", "Load profile picture failed!", e);
                return null;
            }
        }
        return myPicture;
    }

    public static FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("FB", "Access success");
            AccessToken accessToken = loginResult.getAccessToken();

            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //讀出姓名 ID FB個人頁面連結
                            String id = object.optString("id");
                            email = object.optString("email");
                            gender = object.optString("gender");
                            Log.d("FB", "complete");
                            Log.d("FB", id);
                            Log.d("FB", email);
                            Log.d("FB", gender);

                            initUser();
                            MainActivity.setupFbLogin();
                        }
                    });

            //包入你想要得到的資料 送出request
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.d("FB", "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FB", error.toString());
        }
    };

    public static AccessToken getAccessToken() {
        return accessToken;
    }
}
