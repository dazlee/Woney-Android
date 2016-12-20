package com.app.woney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.app.woney.R;
import com.app.woney.activity.MainActivity;
import com.app.woney.data.OngoingData;

public class EarnMainFragment extends Fragment {


    private static TextView topPrice;
    private static TextView nextDraw;
    private static Button luckyDraw;

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupOngoingView();
    }

    private void setupView(View view) {
        topPrice = (TextView) view.findViewById(R.id.earn_top_price);
        nextDraw = (TextView) view.findViewById(R.id.earn_top_draw);
        luckyDraw = (Button) view.findViewById(R.id.earn_bet_btn);

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
