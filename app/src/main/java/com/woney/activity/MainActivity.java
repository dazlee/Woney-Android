package com.woney.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;
import com.woney.R;
import com.woney.adpt.ViewPagerAdapter;
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

public class MainActivity extends AppCompatActivity {

    private static UserData woneyUser;

    CallbackManager callbackManager;
    private static Resources res;

    private static ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.btn_earn_draw,
            R.drawable.btn_winner,
            R.drawable.btn_setting
    };
    private static TextView creditWoney;
    private static InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WoneyKey.initRes(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        setupScreenCust();

        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        creditWoney = (TextView) findViewById(R.id.text_woney_credits);

        initAd();
        loadUserData();
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
    protected void onResume() {
        super.onResume();
        setupScreenCust();
    }

    public FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
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
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void clickWall(View view) {
        TJPlacement offerWall = TapjoyUtil.getOfferWall();
        if (offerWall.isContentReady()) {
            offerWall.showContent();
        }
    }

    public void clickWatch(View view) {
        TJPlacement videoAd = TapjoyUtil.getVideoAd();
        if (videoAd.isContentReady()) {
            videoAd.showContent();
        }
    }

    public void clickFbShare(View view) {
        if (woneyUser.isFbLogin()) {

        } else {
            fbLogin(view);
        }
    }

    public void clickDraw(View view) {
        if (woneyUser.isFbLogin()) {
            if (woneyUser.isEnoughDraw()) {
                woneyUser.draw();
            } else {
                Toast.makeText(getApplicationContext(), "有登入FB但錢不夠", Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(getApplicationContext(), "沒登入", Toast.LENGTH_LONG).show();
            fbLogin(view);
        }
    }

    public void clickHistory(View view) {
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F4ABFC734A9A0D1CBE419F3E2A2D97D2")
                .build();

        interstitialAd.loadAd(adRequest);
    }

    public void clickDailyEarn(View view) throws Exception {
        //if (woneyUser.canEarnDaylyToday()) {
        if (true) {
            UserGainReq gainReq = new UserGainReq(woneyUser, WoneyKey.EARN_DAILY);
            RestClient restClient = new RestClient(gainReq);
            restClient.execute();
            Intent intent = new Intent(MainActivity.this, GainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, BackgainActivity.class);
            startActivity(intent);
        }
    }

    public void clickTour(View view) {
        startActivity(new Intent(MainActivity.this, TourActivity.class));
    }

    public void fbLogin(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, woneyUser.getFbGeneralPermission());
    }

    public void fbLogout(View view) {
        FbLogoutDialog logoutDialog = new FbLogoutDialog();
        logoutDialog.show(getSupportFragmentManager(), "fb_logout_dialog");
    }

    private void setupScreenCust() {
        res = getResources();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void initGAd() {
        //adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //       .addTestDevice(getString(R.string.google));
    }

    public static void setupFbLoginView() {
        View settingView = viewPager.findViewWithTag(R.string.main_tab_setting);
        if (settingView != null) {
            EarnSettingFragment.setupLoginView(settingView);
        }
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
            // TODO
        }
        creditWoney.setText(res.getString(R.string.woney_credits, woney));
    }
}
