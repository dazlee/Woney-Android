package com.app.woney.util;

import com.tapjoy.TJPlacement;

/**
 * Created by houan on 2016/11/29.
 */

public class TapjoyUtil {
    private static TJPlacement offerWall;
    private static TJPlacement videoAd;

    public static TJPlacement getOfferWall() {
        return offerWall;
    }

    public static void setOfferWall(TJPlacement offerWall) {
        TapjoyUtil.offerWall = offerWall;
    }

    public static TJPlacement getVideoAd() {
        return videoAd;
    }

    public static void setVideoAd(TJPlacement videoAd) {
        TapjoyUtil.videoAd = videoAd;
    }
}
