package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;
import com.woney.R;
import com.woney.adpt.ViewPagerAdapter;
import com.woney.fragment.EarnMainFragment;
import com.woney.fragment.EarnSettingFragment;
import com.woney.fragment.EarnWinnerFragment;
import com.woney.fragment.FbLogoutDialog;
import com.woney.util.TapjoyUtil;
import com.woney.util.UserUtil;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private static ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.btn_earn_draw,
            R.drawable.btn_winner,
            R.drawable.btn_setting
    };

    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        LoginManager.getInstance().registerCallback(callbackManager, UserUtil.facebookCallback);

        initScreen();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        UserUtil.initUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    }

    public void clickDailyEarn(View view) throws Exception {
        if (UserUtil.canEarnDaylyToday()) {
            Toast.makeText(getApplicationContext(), "賺取今天任務", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "今天賺過囉", Toast.LENGTH_LONG).show();
        }
    }

    public void clickTour(View view) {
        startActivity(new Intent(MainActivity.this, TourActivity.class));
    }

    public void fbLogin(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, UserUtil.getFbGeneralPermission());
    }

    public void fbLogout(View view) {
        FbLogoutDialog logoutDialog = new FbLogoutDialog();
        logoutDialog.show(getSupportFragmentManager(), "fb_logout_dialog");

    }

    public void clickDraw(View view) {
        if (UserUtil.isFbLogin()) {
            if (UserUtil.isEnoughDraw()) {
                Toast.makeText(getApplicationContext(), "有登入FB且錢夠", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "有登入FB但錢不夠", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "沒登入", Toast.LENGTH_LONG).show();
            fbLogin(view);
        }
    }

    public void clickHistory(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tapjoy.onActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initScreen();
    }

    private void initScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void initGAd() {
        //adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //       .addTestDevice(getString(R.string.google));
    }

    public static void setupFbLogin() {
        View settingView = viewPager.findViewWithTag(R.string.main_tab_setting);
        if (settingView != null) {
            EarnSettingFragment.setupLoginView(settingView);
        }
    }

    public static void setupFbLogout() {
        View settingView = viewPager.findViewWithTag(R.string.main_tab_setting);
        if (settingView != null) {
            EarnSettingFragment.setupLogoutView(settingView);
        }
    }
}
