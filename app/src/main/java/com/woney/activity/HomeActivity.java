package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJEarnedCurrencyListener;
import com.tapjoy.TJError;
import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.woney.R;
import com.woney.data.WoneyKey;
import com.woney.listener.TapjoyVideoListener;
import com.woney.listener.TapjoyWallListener;
import com.woney.req.UserGainReq;
import com.woney.util.RestClient;
import com.woney.util.SystemUtil;
import com.woney.util.TapjoyUtil;

import java.util.Hashtable;

public class HomeActivity extends AppCompatActivity implements TJConnectListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initView();

        initTapjoy();
    }

    private void initView() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initTapjoy() {
        Hashtable hashtable = new Hashtable();
        Tapjoy.connect(getApplicationContext(),
                getString(R.string.tapjoy_sdkKey),
                hashtable, this);

        if (WoneyKey.devMode) {
            Tapjoy.setDebugEnabled(true);
        }
    }

    private void changeActivity() {
        if (SystemUtil.isFirstStarted(getBaseContext())) {
            startActivity(new Intent(HomeActivity.this, TourActivity.class));
        } else {
            if (SystemUtil.isConnectedToInternet(getApplicationContext())) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            } else {
                SystemUtil.showNoConnect(getApplicationContext());
            }
        }
        finish();
    }

    private void requiredPlacements() {
        TJPlacement offerWall;
        TJPlacement videoAd;

        offerWall = Tapjoy.getPlacement("WoneyWall", new TapjoyWallListener());

        videoAd = Tapjoy.getPlacement("WoneyVideo", new TapjoyVideoListener());

        if (Tapjoy.isConnected()) {
            offerWall.requestContent();
            videoAd.requestContent();

            TapjoyUtil.setOfferWall(offerWall);
            TapjoyUtil.setVideoAd(videoAd);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tapjoy.onActivityStart(this);
    }

    @Override
    public void onConnectSuccess() {
        requiredPlacements();

        // TBD
        changeActivity();
    }

    @Override
    public void onConnectFailure() {
        Log.d("Tapjoy", "Connection Failed!");
        SystemUtil.showNoConnect(getApplicationContext());
        finish();
    }
}
