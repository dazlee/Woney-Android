package com.woney.data;

import com.woney.R;
import com.woney.util.SystemUtil;

import org.json.JSONObject;

import java.text.DecimalFormat;
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
}
