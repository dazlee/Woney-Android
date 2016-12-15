package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;
import com.woney.R;
import com.woney.adpt.ViewPagerAdapter;
import com.woney.fragment.Tour1Fragment;
import com.woney.fragment.Tour2Fragment;
import com.woney.fragment.Tour3Fragment;
import com.woney.util.SystemUtil;

public class TourActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tour);

        initView();

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.CirclePageindicator);
        circlePageIndicator.setViewPager(mViewPager);
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

    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Tour1Fragment(), null);
        adapter.addFrag(new Tour2Fragment(), null);
        adapter.addFrag(new Tour3Fragment(), null);
        mViewPager.setAdapter(adapter);
    }

    public void startUse(View view) {
        if (SystemUtil.isFirstStarted(getApplicationContext())) {
            SystemUtil.finishedTour(getBaseContext());
            if (SystemUtil.isConnectedToInternet(getApplicationContext())) {
                startActivity(new Intent(TourActivity.this, MainActivity.class));
            } else {
                SystemUtil.showNoConnect(getApplicationContext());
            }
        } else {
            onBackPressed();
        }
        finish();
    }
}
