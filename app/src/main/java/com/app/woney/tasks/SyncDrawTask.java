package com.app.woney.tasks;

import com.app.woney.activity.MainActivity;
import com.app.woney.req.UserBetReq;
import com.app.woney.util.RestClient;

import java.util.TimerTask;

/**
 * Created by houan on 2016/12/7.
 */

public class SyncDrawTask extends TimerTask {

    public SyncDrawTask() {
        super();
    }

    @Override
    public void run() {
        RestClient restClient = new RestClient(new UserBetReq(MainActivity.getUser().getAccessHeaderMap()));
        restClient.execute();
    }
}
