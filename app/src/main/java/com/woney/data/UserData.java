package com.woney.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.woney.R;
import com.woney.req.FacebookReq;
import com.woney.req.SingUpReq;
import com.woney.util.RestClient;
import com.woney.util.SystemUtil;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by houan on 2016/12/3.
 */

public class UserData {
    private static final List<String> FB_GENERAL_PERMISSION = Arrays.asList("public_profile", "email");
    private static final int drawAtTime = 10;
    private static boolean isUpdate;
    private static Resources res;
    private static ContentValues basicData;

    private AccessToken accessToken;
    private Profile profile;

    private Bitmap myPicture = null;

    // From woney
    private String woneyAccessToken;
    private int woney;
    private int wonTimes;
    private int totalWoney;
    private Date lastDailyEarn;

    public UserData(Resources res) {
        this.basicData = new ContentValues();
        this.res = res;
    }

    public void loadFbData() {
        Profile profile = Profile.getCurrentProfile();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (profile != null && accessToken != null) {
            // To get email and gender
            FacebookReq.sendMeReq(accessToken);
        }
    }

    public void updateByReqCb(String id, String email, String gender) {
        Log.d("User", "Update user date from ID'" + id + "', local ID:'" + getFacebookID() + "'");
        if (accessToken == null || profile == null) {
            accessToken = AccessToken.getCurrentAccessToken();
            profile = Profile.getCurrentProfile();
        }

        isUpdate = false;
        updateArg(getKeyEmail(), getEmail(), email);
        updateArg(getKeyGender(), getGender(), gender);

        if (profile != null) {
            updateArg(getKeyFacebookID(), getFacebookID(), profile.getId());
            updateArg(getKeyFirstName(), getFirstName(), profile.getFirstName());
            updateArg(getKeyLastName(), getLastName(), profile.getLastName());
            updateArg(getKeyMiddleName(), getMiddleName(), profile.getMiddleName());
            updateArg(getKeyPhotoUrl(), getPhotoUrl(), getPhotoUrlByID());
        }
    }

    public void syncWoney() {
        if (isFbLogin()) {
            SingUpReq req = new SingUpReq(this);
            RestClient restClient = new RestClient(req);
            restClient.execute();
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public ContentValues getBasicDataMap() {
        return basicData;
    }

    public String getBasicData(String key) {
        return basicData.getAsString(key);
    }

    public void setBasicData(String key, String value) {
        basicData.put(key, value);
    }

    private String getKeyFacebookID() {
        return res.getString(R.string.user_facebook_id);
    }

    public String getFacebookID() {
        return basicData.getAsString(getKeyFacebookID());
    }

    private String getKeyFirstName() {
        return res.getString(R.string.user_first_name);
    }

    public String getFirstName() {
        return basicData.getAsString(getKeyFirstName());
    }

    private String getKeyLastName() {
        return res.getString(R.string.user_last_name);
    }

    public String getLastName() {
        return basicData.getAsString(getKeyLastName());
    }

    private String getKeyMiddleName() {
        return res.getString(R.string.user_middle_name);
    }

    public String getMiddleName() {
        return basicData.getAsString(getKeyMiddleName());
    }

    private String getKeyEmail() {
        return res.getString(R.string.user_email);
    }

    public String getEmail() {
        return basicData.getAsString(getKeyEmail());
    }

    private String getKeyGender() {
        return res.getString(R.string.user_gender);
    }

    public String getGender() {
        return basicData.getAsString(getKeyGender());
    }

    private String getKeyPhotoUrl() {
        return res.getString(R.string.user_photo_url);
    }

    public String getPhotoUrl() {
        return basicData.getAsString(getKeyPhotoUrl());
    }

    private String getPhotoUrlByID() {
        if (profile != null) {
            return "http://graph.facebook.com/" + profile.getId() + "/picture?type=large";
        }
        return null;
    }

    public int getWoney() {
        return woney;
    }

    public void setFacebookID(String facebookID) {
        basicData.put(getKeyFacebookID(), facebookID);
    }

    public void setFirstName(String firstName) {
        basicData.put(getKeyFirstName(), firstName);
    }

    public void setLastName(String lastName) {
        basicData.put(getKeyLastName(), lastName);
    }

    public void setMiddleName(String middleName) {
        basicData.put(getKeyMiddleName(), middleName);
    }

    public void setEmail(String email) {
        basicData.put(getKeyEmail(), email);
    }

    public void setGender(String gender) {
        basicData.put(getKeyGender(), gender);
    }

    public void setPhotoUrl(String photoUrl) {
        basicData.put(getKeyPhotoUrl(), photoUrl);
    }

    public void setWoney(int woney) {
        this.woney = woney;
    }

    public boolean isEnoughDraw() {
        return (woney > drawAtTime) ? true : false;
    }

    public boolean canEarnDaylyToday() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = formatter.parse(formatter.format(new Date()));

        if (lastDailyEarn == null || lastDailyEarn.before(today)) {
            lastDailyEarn = today;
            return true;
        }
        return false;
    }

    public boolean isFbLogin() {
        return accessToken != null && profile != null ? true : false;
    }

    public List<String> getFbGeneralPermission() {
        return FB_GENERAL_PERMISSION;
    }

    public String getFbName() {
        return (profile != null) ? profile.getName() : null;
    }

    public Bitmap getProfilePicture() {
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

    private void updateArg(String key, String arg1, String arg2) {
        if (arg1 == null || arg1.equals(arg2)) {
            isUpdate = true;
            basicData.put(key, arg2);
        }
    }

    public void saveUpdate(Context context) {
        if (isUpdate) {
            SystemUtil.saveUser(context, this);
            isUpdate = false;
        }
    }

    public void logoutFb() {
        profile = null;
        accessToken = null;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "basic data=" + basicData.toString() +
                ", accessToken=" + accessToken +
                ", woney=" + woney +
                ", woneyToken='" + woneyAccessToken + '\'' +
                ", lastDailyEarn=" + lastDailyEarn +
                '}';
    }
}
