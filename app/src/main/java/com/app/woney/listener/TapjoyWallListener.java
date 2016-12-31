package com.app.woney.listener;

import android.util.Log;

import com.app.woney.fragment.EarnMainFragment;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.app.woney.util.TapjoyUtil;

/**
 * Created by houan on 2016/12/15.
 */

public class TapjoyWallListener implements TJPlacementListener {
    public static boolean isReady = false;
    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        Log.d("Tapjoy", "onRequestSuccess");
        this.isReady = false;
        EarnMainFragment.updateButtonLayout();
    }

    @Override
    public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
        Log.d("Tapjoy", "onRequestFailure");
        this.isReady = false;
        EarnMainFragment.updateButtonLayout();
    }

    @Override
    public void onContentReady(TJPlacement tjPlacement) {
        Log.d("Tapjoy", "onContentReady");
        this.isReady = true;
        EarnMainFragment.updateButtonLayout();
    }

    @Override
    public void onContentShow(TJPlacement tjPlacement) {
        Log.d("Tapjoy", "onContentShow");
    }

    @Override
    public void onContentDismiss(TJPlacement tjPlacement) {
        Log.d("Tapjoy", "onContentDismiss");
        this.isReady = false;
        EarnMainFragment.updateButtonLayout();

        TJPlacement offerWall = TapjoyUtil.getOfferWall();
        if (!offerWall.isContentReady()) {
            offerWall.requestContent();
        }
    }

    @Override
    public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {
        Log.d("Tapjoy", "onPurchaseRequest");
        Log.d("Tapjoy", "String: " + s);
    }

    @Override
    public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
        Log.d("Tapjoy", "onRewardRequest");
        Log.d("Tapjoy", "String: " + s);
        Log.d("Tapjoy", "i: " + i);
    }
}
