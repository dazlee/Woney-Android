package com.woney.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.Profile;
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
    private static final int imgSize = 100;
    private static boolean isUpdate;

    private String facebookID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String gender;
    private Bitmap myPicture = null;

    private AccessToken accessToken;
    private Profile profile;

    private String woneyToken;
    private int woney;
    private Date lastDailyEarn;

    public UserData() {}

    public void updateByFbProfile() {
        accessToken = AccessToken.getCurrentAccessToken();
        profile = Profile.getCurrentProfile();

        if (profile != null) {
            // To get email and gender
            FacebookReq.sendMeReq(accessToken);
        }
    }

    public void updateByReqCb(String id, String email, String gender) {
        Log.e("User", "Update user date from ID'" + id + "', local ID:'" + id + "'");

        isUpdate = false;
        this.email = updateArg(this.email, email);
        this.gender = updateArg(this.gender, gender);

        if (profile != null) {
            this.facebookID = updateArg(facebookID, profile.getId());
            this.firstName = updateArg(firstName, profile.getFirstName());
            this.lastName = updateArg(lastName, profile.getLastName());
            this.middleName = updateArg(middleName, profile.getMiddleName());
        }
    }

    public void syncWoney() {
        SingUpReq req = new SingUpReq();
        RestClient.sendReq(req);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public int getWoney() {
        return woney;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
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

    public String getFacebookID() {
        return facebookID;
    }

    public List<String> getFbGeneralPermission() {
        return FB_GENERAL_PERMISSION;
    }

    public void setMyPicture(Bitmap myPicture) {
        myPicture = myPicture;
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

    private String updateArg(String arg1, String arg2) {
        if (arg1 == null || arg1.equals(arg2)) {
            isUpdate = true;
            return arg2;
        }
        return arg1;
    }

    public void saveUpdate(Context context) {
        if (isUpdate) {
            SystemUtil.saveUser(context, this);
            isUpdate = false;
        }
    }

    @Override
    public String toString() {
        return "UserData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", accessToken=" + accessToken +
                ", woney=" + woney +
                ", woneyToken='" + woneyToken + '\'' +
                ", lastDailyEarn=" + lastDailyEarn +
                '}';
    }
}
