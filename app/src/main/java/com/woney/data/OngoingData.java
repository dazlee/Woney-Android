package com.woney.data;

import com.woney.R;
import com.woney.util.SystemUtil;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by houan on 2016/12/6.
 */

public class OngoingData extends CoreData {

    public OngoingData() {
        super();
    }

    public void updateDataByJson(JSONObject jsonObject) {
        updateValueByJson(WoneyKey.getStringArray(R.array.ongoing_resp), jsonObject);
    }

    public Integer getReward() {
        return values.getAsInteger(WoneyKey.getRewardKey());
    }

    public String getFormatReward() {
        Integer reward = getReward();
        DecimalFormat mDecimalFormat = new DecimalFormat("#,###");
        return mDecimalFormat.format((double) reward);
    }

    public Date getEndTime() {
        return SystemUtil.tzStr2Date(values.getAsString(WoneyKey.getEndTimeKey()));
    }

    public String getID() {
        return values.getAsString(WoneyKey.getIDKey());
    }

    public Integer getSeries() {
        return values.getAsInteger(WoneyKey.getSeriesKey());
    }

    public String getFormatNextDraw() {
        Date endTime = getEndTime();
        Date nowTime = new Date();
        long totalHours = 0;

        if (endTime != null) {
            long diff = endTime.getTime() - nowTime.getTime();
            totalHours = TimeUnit.MILLISECONDS.toHours(diff);
        }

        int leftDays = totalHours < 0 ? 0 : (int) totalHours/24;
        int leftHours = totalHours < 0 ? 0 : (int) totalHours%24;
        return WoneyKey.getStringFormated(R.string.earn_top_draw, leftDays, leftHours);
    }

    public String getFormatFirstDraw() {
        Date endTime = getEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ('GMT'Z) 'on' yyyy/MM/dd");
        return sdf.format(endTime);
    }

    public boolean isFirstSeries() {
        if (getSeries().equals(WoneyKey.lockWinnerSeries)) {
            return true;
        } else {
            return false;
        }
    }

}
