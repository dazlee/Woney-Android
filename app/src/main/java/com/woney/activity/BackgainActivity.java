package com.woney.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.woney.R;

import java.util.Random;

public class BackgainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_backgain);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void clickOK(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Math.random() < 0.5) {
            MainActivity.requestNewInterstitial();
        }
    }
}
