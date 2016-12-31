package com.app.woney.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.woney.listener.TapjoyVideoListener;
import com.app.woney.listener.TapjoyWallListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.app.woney.R;
import com.app.woney.activity.MainActivity;
import com.app.woney.data.OngoingData;
import com.tapjoy.Tapjoy;

public class EarnMainFragment extends Fragment {

    private static TextView topPrice;
    private static TextView nextDraw;
    private static Button luckyDraw;

    private static TextView earnOfferWallCredit;
    private static TextView earnOfferWallStatus;
    private static TextView earnWatchCredit;
    private static TextView earnWatchStatus;

    private static AdView adView;

    public EarnMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earn_main, container, false);
        view.setTag(R.string.main_tab_main);

        setupView(view);
        setupAd(view);
        initButtons();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupOngoingView();
        initButtons();
    }

    private void setupView(View view) {
        topPrice = (TextView) view.findViewById(R.id.earn_top_price);
        nextDraw = (TextView) view.findViewById(R.id.earn_top_draw);
        luckyDraw = (Button) view.findViewById(R.id.earn_bet_btn);

        earnOfferWallCredit = (TextView) view.findViewById(R.id.earn_offer_wall_credits);
        earnOfferWallStatus = (TextView) view.findViewById(R.id.earn_offer_wall_status);
        earnWatchCredit = (TextView) view.findViewById(R.id.earn_watch_credits);
        earnWatchStatus = (TextView) view.findViewById(R.id.earn_watch_status);

        setupBetsBtn();
    }

    private void loadOngoing() {
        setupOngoingView();
    }

    public static void setupOngoingView() {
        OngoingData ongoingData = OngoingData.getOngoingData();
        if (ongoingData != null) {
            topPrice.setText(ongoingData.getFormatReward());
            nextDraw.setText(ongoingData.getFormatNextDraw());
        }
    }

    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (TapjoyWallListener.isReady) {
                if (earnOfferWallCredit != null && earnOfferWallStatus != null) {
                    earnOfferWallCredit.setVisibility(View.VISIBLE);
                    earnOfferWallStatus.setVisibility(View.GONE);
                }
            } else {
                if (earnOfferWallCredit != null && earnOfferWallStatus != null) {
                    earnOfferWallCredit.setVisibility(View.GONE);
                    earnOfferWallStatus.setVisibility(View.VISIBLE);
                }
            }
            if (TapjoyVideoListener.isReady) {
                if (earnWatchCredit != null && earnWatchStatus != null) {
                    earnWatchCredit.setVisibility(View.VISIBLE);
                    earnWatchStatus.setVisibility(View.GONE);
                }
            } else {
                if (earnWatchCredit != null && earnWatchStatus != null) {
                    earnWatchCredit.setVisibility(View.GONE);
                    earnWatchStatus.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public void initButtons () {
        if (TapjoyWallListener.isReady) {
            if (earnOfferWallCredit != null && earnOfferWallStatus != null) {
                earnOfferWallCredit.setVisibility(View.VISIBLE);
                earnOfferWallStatus.setVisibility(View.GONE);
            }
        } else {
            if (earnOfferWallCredit != null && earnOfferWallStatus != null) {
                earnOfferWallCredit.setVisibility(View.GONE);
                earnOfferWallStatus.setVisibility(View.VISIBLE);
            }
        }
        if (TapjoyVideoListener.isReady) {
            if (earnWatchCredit != null && earnWatchStatus != null) {
                earnWatchCredit.setVisibility(View.VISIBLE);
                earnWatchStatus.setVisibility(View.GONE);
            }
        } else {
            if (earnWatchCredit != null && earnWatchStatus != null) {
                earnWatchCredit.setVisibility(View.GONE);
                earnWatchStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void updateButtonLayout() {
        mHandler.sendMessage(new Message());
    }

    public static void setupBetsBtn() {
        String bets = "0";
        if (MainActivity.getUser() != null) {
            bets = MainActivity.getUser().getFormatLukDraw();
        }
        luckyDraw.setText(bets);
    }

    public void setupAd(View view) {
        adView = (AdView) view.findViewById(R.id.earn_ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F4ABFC734A9A0D1CBE419F3E2A2D97D2").build();
        adView.loadAd(adRequest);
    }
}
