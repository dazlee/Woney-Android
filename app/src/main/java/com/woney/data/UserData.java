package com.woney.data;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Created by houan on 2016/12/3.
 */

public class UserData extends CoreData {
    private static final List<String> FB_GENERAL_PERMISSION = Arrays.asList("public_profile", "email");
    private boolean isLoadFb;
    private boolean isLoadWoney;

    private static final int delaySync = 1500;
    private Timer timer = new Timer(true);
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
            updateValue(WoneyKey.getDisplayNameKey(), getDisplayName(), profile.getName());
        }
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getFacebookID() {
        return getStringByKey(WoneyKey.getFacebookIDKey());
    }

    public String getDisplayName() {
        return getStringByKey(WoneyKey.getDisplayNameKey());
    }

    public String getFirstName() {
        return getStringByKey(WoneyKey.getFirstNameKey());
    }

    public String getLastName() {
        return getStringByKey(WoneyKey.getLastNameKey());
    }

    public String getMiddleName() {
        return getStringByKey(WoneyKey.getMiddleNameKey());
    }

    public String getEmail() {
        return getStringByKey(WoneyKey.getEmailKey());
    }

    public String getGender() {
        return getStringByKey(WoneyKey.getGenderKey());
    }

    public String getPhotoUrl() {
        return getStringByKey(WoneyKey.getPhotoUrlKey());
    }

    private String getPhotoUrlByID() {
        if (profile != null) {
            return "http://graph.facebook.com/" + profile.getId() + "/picture?type=large";
        }
        return null;
    }

    public Integer getWoney() {
        return getIntegerByKey(WoneyKey.getWoneyKey());
    }

    public Integer getTotalWoney() {
        return getIntegerByKey(WoneyKey.getTotalWoneyKey());
    }

    public Integer getBets() {
        return getIntegerByKey(WoneyKey.getBetsKey());
    }

    public Date getLastDailyEarn() {
        return SystemUtil.tzStr2Date(getStringByKey(WoneyKey.getLastDailyEarnKey()));
    }

    public Date getLastFbShare() {
        return SystemUtil.tzStr2Date(getStringByKey(WoneyKey.getLastFbShareKey()));
    }

    public String getShowName() {
        String displayName = getDisplayName();
        if (displayName != null && displayName != "") {
            return displayName;
        } else {
            return getFirstName() + " " + getLastName();
        }
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

    public void setDisplayName(String displayName) {
        values.put(WoneyKey.getDisplayNameKey(), displayName);
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
        String key = WoneyKey.getWoneyKey();
        values.put(key, woney);
        SystemUtil.saveStringValue(key, String.valueOf(woney));
    }

    public void setLastDailyEarn(Date lastDailyEarn) {
        values.put(WoneyKey.getLastDailyEarnKey(), SystemUtil.date2TzStr(lastDailyEarn));
    }

    public void setLastFbShare(Date lastFbShare) {
        values.put(WoneyKey.getLastFbShareKey(), SystemUtil.date2TzStr(lastFbShare));
    }

    public void setTotalWoney(int totalWoney) {
        String key = WoneyKey.getTotalWoneyKey();
        values.put(key, totalWoney);
        SystemUtil.saveStringValue(key, String.valueOf(totalWoney));
    }

    public void setBets(Integer bets) {
        String key = WoneyKey.getBetsKey();
        values.put(WoneyKey.getBetsKey(), bets);
        SystemUtil.saveStringValue(key, String.valueOf(bets));
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

    public boolean canShareFb() {
        Date lastFbShare = getLastFbShare();

        if (lastFbShare != null && getFbShareUnlockTime().after(new Date())) {
            return false;
        }

        return true;
    }

    public Date getFbShareUnlockTime() {
        Date lastFbShare = getLastFbShare();
        if (lastFbShare != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastFbShare);
            cal.add(Calendar.HOUR_OF_DAY, WoneyKey.intervalShareHR);
            return cal.getTime();
        }
        return null;
    }

    public boolean isFbLogin() {
        return accessToken != null && profile != null ? true : false;
    }

    public List<String> getFbGeneralPermission() {
        return FB_GENERAL_PERMISSION;
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

    public void clearData() {
        setWoney(0);
        setBets(0);
        SystemUtil.saveUser(this);
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
            header.put(key, getStringByKey(key));
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
