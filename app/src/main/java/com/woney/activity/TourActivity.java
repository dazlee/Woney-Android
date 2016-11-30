package com.woney.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Tour1Fragment(), null);
        adapter.addFrag(new Tour2Fragment(), null);
        adapter.addFrag(new Tour3Fragment(), null);
        mViewPager.setAdapter(adapter);
    }

    public void startUse(View view) {
        SystemUtil.finishedTour(getBaseContext());
        startActivity(new Intent(TourActivity.this, MainActivity.class));
        finish();
    }
}
