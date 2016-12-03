package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.woney.R;
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

        Tapjoy.setDebugEnabled(true);
    }

    private void changeActivity() {
        if (SystemUtil.isFirstStarted(getBaseContext())) {
            startActivity(new Intent(HomeActivity.this, TourActivity.class));
        } else {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }
        finish();
    }

    private void requiredPlacements() {
        TJPlacement offerWall;
        TJPlacement videoAd;

        offerWall = Tapjoy.getPlacement("OfferWall", new TJPlacementListener() {
            @Override
            public void onRequestSuccess(TJPlacement tjPlacement) {
                Log.d("Tapjoy", "onRequestSuccess");
            }

            @Override
            public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
                Log.d("Tapjoy", "onRequestFailure");
            }

            @Override
            public void onContentReady(TJPlacement tjPlacement) {
                Log.d("Tapjoy", "onContentReady");
            }

            @Override
            public void onContentShow(TJPlacement tjPlacement) {
                Log.d("Tapjoy", "");
            }

            @Override
            public void onContentDismiss(TJPlacement tjPlacement) {
                Log.d("Tapjoy", "onContentShow");
            }

            @Override
            public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {
                Log.d("Tapjoy", "onPurchaseRequest");
            }

            @Override
            public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
                Log.d("Tapjoy", "onRewardRequest");
            }
        });

        videoAd = Tapjoy.getPlacement("VideoAd", new TJPlacementListener() {
            @Override
            public void onRequestSuccess(TJPlacement tjPlacement) {

            }

            @Override
            public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {

            }

            @Override
            public void onContentReady(TJPlacement tjPlacement) {

            }

            @Override
            public void onContentShow(TJPlacement tjPlacement) {

            }

            @Override
            public void onContentDismiss(TJPlacement tjPlacement) {

            }

            @Override
            public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

            }

            @Override
            public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {

            }
        });

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
    }
}
