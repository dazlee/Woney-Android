package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;
import com.woney.R;
import com.woney.adpt.ViewPagerAdapter;
import com.woney.fragment.EarnMainFragment;
import com.woney.fragment.EarnSettingFragment;
import com.woney.fragment.EarnWinnerFragment;
import com.woney.util.FacebookUtil;
import com.woney.util.TapjoyUtil;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
        LoginManager.getInstance().registerCallback(callbackManager, FacebookUtil.facebookCallback);

        initScreen();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
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

    public void goToWall(View view) {
        TJPlacement offerWall = TapjoyUtil.getOfferWall();
        if (offerWall.isContentReady()) {
            offerWall.showContent();
        }
    }

    public void goToWatch(View view) {
        TJPlacement videoAd = TapjoyUtil.getVideoAd();
        if (videoAd.isContentReady()) {
            videoAd.showContent();
        }
    }

    public void goToShare(View view) {
    }

    public void goToDaily(View view) {
        //startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    public void goToTour(View view) {
        startActivity(new Intent(MainActivity.this, TourActivity.class));
    }

    public void fbLogin(View view) {
        //Button loginButton = (Button) view.findViewById(R.id.setting_fb_login);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    public void fbLogout(View view) {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tapjoy.onActivityStop(this);
    }

    private void initScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void initGAd() {
        //adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //       .addTestDevice(getString(R.string.google));
    }
}
