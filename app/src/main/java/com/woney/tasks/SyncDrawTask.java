package com.woney.tasks;

import com.woney.activity.MainActivity;
import com.woney.req.UserBetReq;
import com.woney.util.RestClient;

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
        RestClient restClient = new RestClient(new UserBetReq(MainActivity.getUser()));
        restClient.execute();
    }
}
