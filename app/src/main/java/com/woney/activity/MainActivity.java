package com.woney.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tapjoy.TJEarnedCurrencyListener;
import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;
import com.woney.R;
import com.woney.adpt.ViewPagerAdapter;
import com.woney.data.OngoingData;
import com.woney.data.UserData;
import com.woney.data.WoneyKey;
import com.woney.fragment.EarnMainFragment;
import com.woney.fragment.EarnSettingFragment;
import com.woney.fragment.EarnWinnerFragment;
import com.woney.fragment.FbLogoutDialog;
import com.woney.req.FacebookReq;
import com.woney.req.UserGainReq;
import com.woney.util.RestClient;
import com.woney.util.SystemUtil;
import com.woney.util.TapjoyUtil;
import com.woney.view.WoneyViewPager;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static UserData woneyUser;

    CallbackManager callbackManager;

    private static WoneyViewPager viewPager;
    private static TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.btn_earn_draw,
            R.drawable.btn_winner,
            R.drawable.btn_setting
    };
    private static TextView creditWoney;
    private static InterstitialAd interstitialAd;
    private static ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WoneyKey.initRes(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        LoginManager.getInstance().registerCallback(callbackManager, connectCallback);
        shareDialog = new ShareDialog(this);

        viewPager = (WoneyViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        creditWoney = (TextView) findViewById(R.id.text_woney_credits);

        initAd();
        loadUserData();
        setupTapjoy();
        setupWoneyCreditView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tapjoy.onActivityStop(this);
    }

    @Override
    public void onBackPressed() {
        if (EarnSettingFragment.pressBack()) {
            super.onBackPressed();
        }
    }

    public FacebookCallback<LoginResult> connectCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("FB", "Access success");
            AccessToken accessToken = loginResult.getAccessToken();
            FacebookReq.loginFb(accessToken);
        }

        @Override
        public void onCancel() {
            Log.d("FB", "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FB", error.toString());
        }
    };

    public FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FB", "Post success");

            Log.d("GainReq", "FB Share, Earn: " + WoneyKey.EARN_FB_SHARE);
            UserGainReq req = new UserGainReq(woneyUser, WoneyKey.EARN_FB_SHARE, false, true);
            RestClient restClient = new RestClient(req);
            restClient.execute();
            askGainActivity(WoneyKey.EARN_FB_SHARE);
            woneyUser.setLastFbShare(new Date());
        }

        @Override
        public void onCancel() {
            Log.d("FB", "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FB", error.toString());
        }
    };

    private void initAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.gad_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }
        });
    }

    private void setupTapjoy() {
        Tapjoy.setEarnedCurrencyListener(new TJEarnedCurrencyListener() {
            @Override
            public void onEarnedCurrency(String currencyName, int amount) {
                Log.d("Tapjoy", "Currency name: " + currencyName + ", " + "Balance: " + amount);
                UserGainReq req = new UserGainReq(MainActivity.getUser(), amount);
                RestClient restClient = new RestClient(req);
                restClient.execute();

                askGainActivity(amount);
            }
        });

        Tapjoy.getCurrencyBalance(new TJGetCurrencyBalanceListener() {
            @Override
            public void onGetCurrencyBalanceResponse(String currencyName, int balance) {
                Log.i("Tapjoy", "getCurrencyBalance returned " + currencyName + ":" + balance);
            }

            @Override
            public void onGetCurrencyBalanceResponseFailure(String error) {
                Log.i("Tapjoy", "getCurrencyBalance error:  " + error);
            }
        });
    }

    public static UserData getUser() {
        return woneyUser;
    }

    private void loadUserData() {
        woneyUser = SystemUtil.loadUser();
        woneyUser.loadFbData();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EarnMainFragment(), getString(R.string.main_tab_main));
        adapter.addFrag(new EarnWinnerFragment(), getString(R.string.main_tab_winner));
        adapter.addFrag(new EarnSettingFragment(), getString(R.string.main_tab_setting));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        //tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab, null);
        tabOne.setText(getString(R.string.main_tab_main));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab, null);
        tabTwo.setText(getString(R.string.main_tab_winner));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.tab, null);
        tabThree.setText(getString(R.string.main_tab_setting));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        if (!EarnMainFragment.getOngoingData().isFirstSeries()) {
                            viewPager.setCurrentItem(1);
                        } else {
                            startActivity(new Intent(MainActivity.this, WinDialog.class));
                        }
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void clickWall(View view) {
        TJPlacement offerWall = TapjoyUtil.getOfferWall();
        if (offerWall.isContentReady()) {
            offerWall.showContent();
        } else {
            offerWall.requestContent();
        }
    }

    public void clickWatch(View view) {
        TJPlacement videoAd = TapjoyUtil.getVideoAd();
        if (videoAd.isContentReady()) {
            videoAd.showContent();
        } else {
            videoAd.requestContent();
        }
    }

    public void clickFbShare(View view) {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.canShareFb()) {
                if (shareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("I use an app claaed EARN CASH")
                            .setContentDescription("Share app link")
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();
                    shareDialog.registerCallback(callbackManager, shareCallback);
                    shareDialog.show(linkContent);

                }
            } else {
                // TODO
                startActivity(new Intent(MainActivity.this, CountdownActivity.class));
            }
        } else {
            fbLogin(view);
        }
    }

    public void clickDailyEarn(View view) throws Exception {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.canEarnDaylyToday()) {
                Log.d("DailyEarn", "Earn : " + WoneyKey.EARN_DAILY);
                UserGainReq gainReq = new UserGainReq(woneyUser, WoneyKey.EARN_DAILY, true, false);
                RestClient restClient = new RestClient(gainReq);
                restClient.execute();
                askGainActivity(WoneyKey.EARN_DAILY);
            } else {
                startActivity(new Intent(MainActivity.this, BackgainActivity.class));
            }
        } else {
            woneyUser.setWoney(woneyUser.getWoney() + WoneyKey.EARN_DAILY);
            setupWoneyCreditView();
        }
    }

    public void clickDraw(View view) {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.isEnoughDraw()) {
                woneyUser.draw();
            } else {
                // TODO
                Toast.makeText(getApplicationContext(), "有登入FB但錢不夠", Toast.LENGTH_LONG).show();
            }
        } else {
            fbLogin(view);
        }
    }

    public void clickHistory(View view) {
        //requestNewInterstitial();
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F4ABFC734A9A0D1CBE419F3E2A2D97D2")
                .build();

        interstitialAd.loadAd(adRequest);
    }

    public void clickHow(View view) {
        EarnSettingFragment.clickHow();
    }

    public void clickTour(View view) {
        startActivity(new Intent(MainActivity.this, TourActivity.class));
    }

    public void clickContact(View view) {
        startActivity(new Intent(MainActivity.this, ContactActivity.class));
    }

    public void clickFanpage(View view) {
        String urlStr = SystemUtil.getFbLink(getApplicationContext());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlStr));
        startActivity(intent);
    }

    public void fbLogin(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, woneyUser.getFbGeneralPermission());
    }

    public void fbLogout(View view) {
        FbLogoutDialog logoutDialog = new FbLogoutDialog();
        logoutDialog.show(getSupportFragmentManager(), "fb_logout_dialog");
    }

    public static void setupFbLogoutView() {
        View settingView = viewPager.findViewWithTag(R.string.main_tab_setting);
        if (settingView != null) {
            EarnSettingFragment.setupLogoutView(settingView);
        }
    }

    public static void setupWoneyCreditView() {
        Integer woney = 0;
        if (getUser() != null && getUser().getWoney() !=null) {
            woney = getUser().getWoney();
        }
        creditWoney.setText(WoneyKey.getStringFormated(R.string.woney_credits, woney));
    }

    private void askGainActivity(Integer gain) {
        Intent intent = new Intent(MainActivity.this, GainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WoneyKey.GAIN_KEY, gain);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static void setViewPagerTouch(OngoingData ongoingData) {
        if (viewPager != null && ongoingData.isFirstSeries()) {
            viewPager.setPagingEnabled(false);
        }
    }
}
