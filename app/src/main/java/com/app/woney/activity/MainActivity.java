package com.app.woney.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.woney.R;
import com.app.woney.adpt.ViewPagerAdapter;
import com.app.woney.data.OngoingData;
import com.app.woney.data.UserData;
import com.app.woney.data.WoneyKey;
import com.app.woney.dialog.BackgainDialog;
import com.app.woney.dialog.ContactDialog;
import com.app.woney.dialog.CountdownDialog;
import com.app.woney.dialog.GainDialog;
import com.app.woney.dialog.MsgDialog;
import com.app.woney.dialog.WinDialog;
import com.app.woney.fragment.EarnMainFragment;
import com.app.woney.fragment.EarnSettingFragment;
import com.app.woney.fragment.EarnWinnerFragment;
import com.app.woney.fragment.FbLogoutDialog;
import com.app.woney.req.FacebookReq;
import com.app.woney.req.GameLastDrawReq;
import com.app.woney.req.GameOnGoingReq;
import com.app.woney.req.UserGainReq;
import com.app.woney.util.RestClient;
import com.app.woney.util.SystemUtil;
import com.app.woney.util.TapjoyUtil;
import com.app.woney.view.WoneyViewPager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
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

    private static int currentPosition;

    private Handler handler = new Handler();

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

        // move this initial from onStart because it will cause some weird race condition
        // the reason is that when tapjoy activity is destroyed, android will call onStart, which will call loadUserData
        // at the same time, other thread is using "profile" to checking if the user is logged in.
        // sometimes user is logged in, sometimes is not. so move load use data to onCreate
        loadUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onStart() {
        super.onStart();
        Tapjoy.onActivityStart(this);
        setupTapjoy();
        setupWoneyCreditView();
        loadOnGoing();
        loadLastDraw();
    }

    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (EarnSettingFragment.pressBack()) {
            super.onBackPressed();
        }
    }

    public FacebookCallback<LoginResult> connectCallback = new FacebookCallback<LoginResult>() {

        private ProfileTracker profileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("FB", "Access success");
            final AccessToken accessToken = loginResult.getAccessToken();
            if (Profile.getCurrentProfile() == null) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Log.d("ProfileTracker", "Loaded user: " + currentProfile.getName());
                        profileTracker.stopTracking();
                        FacebookReq.loadFbData(accessToken, true);
                    }
                };
            } else {
                FacebookReq.loadFbData(accessToken, true);
            }
        }

        @Override
        public void onCancel() {
            Log.d("FB", "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FB", error.getMessage());
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
            askGainDialog(WoneyKey.EARN_FB_SHARE);
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

                UserData user = MainActivity.getUser();
                if (user.isFbLogin()) {
                    UserGainReq req = new UserGainReq(user, amount);
                    RestClient restClient = new RestClient(req);
                    restClient.execute();
                } else {
                    user.setOfflineWoney(user.getOfflineWoney() + amount);

                    // update setupWoneyCreditView
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupWoneyCreditView();
                        }
                    });
                }

                askGainDialog(amount);
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
            public void onTabSelected(final TabLayout.Tab tab) {
                currentPosition = tab.getPosition();

                if (currentPosition == 1) {
                    OngoingData ongoingData = OngoingData.getOngoingData();
                    if (ongoingData == null || ongoingData.isFirstSeries()) {
                        return;
                    }
                }

                if (currentPosition == 2 && EarnSettingFragment.isHowItWorkShow()) {
                    onBackPressed();
                }

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        LinearLayout winnerTab = ((LinearLayout) tabLayout.getChildAt(0));
        Log.d("Main", "Tab count: " + winnerTab.getChildCount());
        winnerTab.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OngoingData ongoingData = OngoingData.getOngoingData();
                if (ongoingData != null && !ongoingData.isFirstSeries()) {
                    if (!EarnWinnerFragment.isAlreadyShowAd()) {
                        handler.postDelayed(WoneyKey.delayAdShow, WoneyKey.winnerDelayAdMillsec);
                    }
                } else {
                    startActivity(new Intent(MainActivity.this, WinDialog.class));
                    return;
                }
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
                            .setContentUrl(Uri.parse(WoneyKey.PLAY_URL))
                            .build();
                    shareDialog.registerCallback(callbackManager, shareCallback);
                    shareDialog.show(linkContent);

                }
            } else {
                startActivity(new Intent(MainActivity.this, CountdownDialog.class));
            }
        } else {
            fbLogin(view);
        }
    }

    public void clickDailyEarn(View view) throws Exception {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.canEarnDaylyToday()) {
                if (woneyUser.isFbLogin()) {
                    Log.d("DailyEarn", "Earn : " + WoneyKey.EARN_DAILY);
                    UserGainReq gainReq = new UserGainReq(woneyUser, WoneyKey.EARN_DAILY, true, false);
                    RestClient restClient = new RestClient(gainReq);
                    restClient.execute();
                } else {
                    woneyUser.setOfflineWoney(woneyUser.getOfflineWoney() + WoneyKey.EARN_DAILY);
                    setupWoneyCreditView();
                }
                askGainDialog(WoneyKey.EARN_DAILY);
            } else {
                startActivity(new Intent(MainActivity.this, BackgainDialog.class));
            }
        } else {
            fbLogin(view);
        }
    }

    public void clickDraw(View view) {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.isEnoughDraw()) {
                woneyUser.draw();
            } else {
                askMsgDialog(getString(R.string.dialog_no_woney));
            }
        } else {
            fbLogin(view);
        }
    }

    public void clickHistory(View view) {
        clickFanpage(view);
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("F4ABFC734A9A0D1CBE419F3E2A2D97D2")
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
        startActivity(new Intent(MainActivity.this, ContactDialog.class));
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
            EarnSettingFragment.setupFbLogoutView(settingView);
        }
    }

    public static void setupWoneyCreditView() {
        Integer woney = 0;
        if (getUser().isFbLogin() && getUser().getUserAccessToken() != null) {
            if (getUser() != null && getUser().getWoney() !=null) {
                woney = getUser().getWoney();
            }
        } else {
            if (getUser() != null && getUser().getOfflineWoney() !=null) {
                woney = getUser().getOfflineWoney();
            }
        }

        creditWoney.setText(WoneyKey.getStringFormated(R.string.woney_credits, woney));
    }

    private void askGainDialog(Integer gain) {
        Intent intent = new Intent(MainActivity.this, GainDialog.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WoneyKey.GAIN_KEY, gain);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void askMsgDialog(String content) {
        Intent intent = new Intent(MainActivity.this, MsgDialog.class);
        Bundle bundle = new Bundle();
        bundle.putString(WoneyKey.TEXT_KEY, content);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static void setViewPagerTouch(OngoingData ongoingData) {
        if (viewPager != null && ongoingData.isFirstSeries()) {
            viewPager.setPagingEnabled(false);
        }
    }

    private void loadOnGoing () {
        GameOnGoingReq req = new GameOnGoingReq();
        RestClient restClient = new RestClient(req);
        restClient.execute();
    }
    private void loadLastDraw() {
        GameLastDrawReq req = new GameLastDrawReq();
        RestClient restClient = new RestClient(req);
        restClient.execute();
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }
}
