package com.woney.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.woney.R;
import com.woney.activity.MainActivity;
import com.woney.fragment.EarnMainFragment;
import com.woney.req.FacebookReq;
import com.woney.timer.SyncDrawTask;
import com.woney.util.SystemUtil;

import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by houan on 2016/12/3.
 */

public class UserData extends CoreData {
    private static final List<String> FB_GENERAL_PERMISSION = Arrays.asList("public_profile", "email");
    private static boolean isLoadFb;
    private static boolean isLoadWoney;

    private Timer timer = new Timer(true);
    private static final int delaySync = 1500;
    private SyncDrawTask syncDrawTask;

    private AccessToken accessToken;
    private Profile profile;

    public UserData() {
        super();
        resetUpdateFlag();
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
        updateValue(WoneyKey.getEmailKey(), getEmail(), email);
        updateValue(WoneyKey.getGenderKey(), getGender(), gender);

        if (profile != null) {
            updateValue(WoneyKey.getFacebookIDKey(), getFacebookID(), profile.getId());
            updateValue(WoneyKey.getFirstNameKey(), getFirstName(), profile.getFirstName());
            updateValue(WoneyKey.getLastNameKey(), getLastName(), profile.getLastName());
            updateValue(WoneyKey.getMiddleNameKey(), getMiddleName(), profile.getMiddleName());
            updateValue(WoneyKey.getPhotoUrlKey(), getPhotoUrl(), getPhotoUrlByID());
            updateValue(WoneyKey.getNameKey(), getName(), profile.getName());
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getFacebookID() {
        return values.getAsString(WoneyKey.getFacebookIDKey());
    }

    public String getFirstName() {
        return values.getAsString(WoneyKey.getFirstNameKey());
    }

    public String getLastName() {
        return values.getAsString(WoneyKey.getLastNameKey());
    }

    public String getMiddleName() {
        return values.getAsString(WoneyKey.getMiddleNameKey());
    }

    public String getEmail() {
        return values.getAsString(WoneyKey.getEmailKey());
    }

    public String getGender() {
        return values.getAsString(WoneyKey.getGenderKey());
    }

    public String getPhotoUrl() {
        return values.getAsString(WoneyKey.getPhotoUrlKey());
    }

    private String getPhotoUrlByID() {
        if (profile != null) {
            return "http://graph.facebook.com/" + profile.getId() + "/picture?type=large";
        }
        return null;
    }

    public Integer getWoney() {
        return values.getAsInteger(WoneyKey.getWoneyKey());
    }

    public Integer getTotalWoney() {
        return values.getAsInteger(WoneyKey.getTotalWoneyKey());
    }

    public Integer getBets() {
        return values.getAsInteger(WoneyKey.getBetsKey());
    }

    public Date getLastDailyEarn() {
        return SystemUtil.tzStr2Date(values.getAsString(WoneyKey.getLastDailyEarnKey()));
    }

    public void setFacebookID(String facebookID) {
        values.put(WoneyKey.getFacebookIDKey(), facebookID);
    }

    public void setFirstName(String firstName) {
        values.put(WoneyKey.getFirstNameKey(), firstName);
    }

    public void setLastName(String lastName) {
        values.put(WoneyKey.getLastNameKey(), lastName);
    }

    public void setMiddleName(String middleName) {
        values.put(WoneyKey.getMiddleNameKey(), middleName);
    }

    public void setEmail(String email) {
        values.put(WoneyKey.getEmailKey(), email);
    }

    public void setGender(String gender) {
        values.put(WoneyKey.getGenderKey(), gender);
    }

    public void setPhotoUrl(String photoUrl) {
        values.put(WoneyKey.getPhotoUrlKey(), photoUrl);
    }

    public void setWoney(int woney) {
        values.put(WoneyKey.getWoneyKey(), woney);
    }

    public void setLastDailyEarn(Date lastDailyEarn) {
        values.put(WoneyKey.getLastDailyEarnKey(), SystemUtil.date2TzStr(lastDailyEarn));
    }

    public void setTotalWoney(int totalWoney) {
        values.put(WoneyKey.getTotalWoneyKey(), totalWoney);
    }

    public void setBets(Integer bets) {
        values.put(WoneyKey.getBetsKey(), bets);
    }

    public String getFormatLukDraw() {
        Integer bets = getBets();
        if (bets == null) {
            bets = 0;
            // TODO
        }
        return WoneyKey.getStringFormated(R.string.earn_top_btn_luk, bets);
    }

    public boolean isEnoughDraw() {
        return (getWoney() >= WoneyKey.woneyPerBets) ? true : false;
    }

    public boolean canEarnDaylyToday() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = formatter.parse(formatter.format(new Date()));
        Date lastDailyEarn = getLastDailyEarn();

        if (lastDailyEarn == null || lastDailyEarn.before(today)) {
            setLastDailyEarn(today);
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

    public String getName() {
        return (profile != null) ? profile.getName() : null;
    }

    private void saveUpdate() {
        if (isLoadFb && isLoadWoney && isUpdate) {
            SystemUtil.saveUser(this);
            resetUpdateFlag();
        }
    }

    public void logoutFb() {
        profile = null;
        accessToken = null;
    }

    public void finishLoadFb() {
        this.isLoadFb = true;
        saveUpdate();
    }

    public void finishLoadWoney() {
        this.isLoadWoney = true;
        saveUpdate();
    }

    private void resetUpdateFlag() {
        this.isUpdate = false;
        this.isLoadFb = false;
        this.isLoadWoney = false;
    }

    public void updateWoneyDataByJson(JSONObject jsonObject) {
        updateValueByJson(WoneyKey.getWoneyDataKeyArray(), jsonObject);
    }

    public void updateUserDataByJson(JSONObject jsonObject) {
        updateValueByJson(WoneyKey.getFbKeyArray(), jsonObject);
    }

    public JSONObject getUserReq() {
        return getReqJson(WoneyKey.getFbKeyArray());
    }

    public Map<String, String> getAccessHeaderMap() {
        Map<String, String> header = new HashMap<>();
        for (String key : WoneyKey.getAccessKeyArray()) {
            header.put(key, values.getAsString(key));
        }
        return header;
    }

    public synchronized void addWoney(Integer gain) {
        setWoney(getWoney() + gain);
        setTotalWoney(getTotalWoney());
    }

    public synchronized void lessWoney(Integer less) {
        setWoney(getWoney() - less);
    }

    public synchronized void addBets(Integer add) {
        setBets(getBets() + add);
    }

    public synchronized void draw() {
        lessWoney(WoneyKey.woneyPerBets);
        addBets(WoneyKey.betsPerClick);
        MainActivity.setupWoneyCreditView();
        EarnMainFragment.setupBetsBtn();

        if (syncDrawTask != null) {
            syncDrawTask.cancel();
            clearSyncDrawTime();
        }

        syncDrawTask = new SyncDrawTask();
        timer.schedule(syncDrawTask, delaySync);
    }

    public void clearSyncDrawTime() {
        syncDrawTask = null;
    }
}
