package com.app.woney.data;

import android.util.Log;

import com.app.woney.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by houan on 2016/12/11.
 */

public class LastDrawData extends OngoingData {

    private UserData firstWinner;
    private UserData []commonWinners;

    public LastDrawData() {
        super();
    }

    public void setFirstWinnerByJson(JSONObject jsonObject) {
        try {
            JSONObject first = jsonObject.getJSONObject(WoneyKey.getFirstWinnerKey());
            firstWinner = new UserData();
            firstWinner.updateUserDataByJson(first);

            if (first.has(WoneyKey.getNameKey())) {
                JSONObject nameJson = first.getJSONObject(WoneyKey.getNameKey());
                firstWinner.setLastName(nameJson.getString(WoneyKey.getLastNameKey()));
                firstWinner.setFirstName(nameJson.getString(WoneyKey.getFirstNameKey()));
                firstWinner.setDisplayName(nameJson.getString(WoneyKey.getDisplayNameKey()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCommonWinnersByJson(JSONObject jsonObject) {
        Log.d("","");

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(WoneyKey.getCommonWinnersKey());

            commonWinners = new UserData[jsonArray.length()];
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject userJson = jsonArray.getJSONObject(i);
                commonWinners[i] = new UserData();
                commonWinners[i].updateUserDataByJson(userJson);

                if (userJson.has(WoneyKey.getNameKey())) {
                    JSONObject nameJson = userJson.getJSONObject(WoneyKey.getNameKey());
                    commonWinners[i].setLastName(nameJson.getString(WoneyKey.getLastNameKey()));
                    commonWinners[i].setFirstName(nameJson.getString(WoneyKey.getFirstNameKey()));
                    commonWinners[i].setDisplayName(nameJson.getString(WoneyKey.getDisplayNameKey()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserData getFirstWinner() {
        return firstWinner;
    }

    public UserData[] getCommonWinners() {
        return commonWinners;
    }

    public Integer getSecReward() {
        return values.getAsInteger(WoneyKey.getSecRewardKey());
    }

    public String getFormatedTopText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateStr = "";
        if (getEndTime() != null) {
            dateStr = sdf.format(getEndTime());
        }
        return WoneyKey.getStringFormated(R.string.winner_winner, dateStr);
    }

    public String getFormatedSecReward() {
        return WoneyKey.getStringFormated(R.string.winner_sec_price, getSecReward());
    }
}
