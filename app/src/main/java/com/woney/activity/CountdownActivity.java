package com.woney.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.woney.R;
import com.woney.util.SystemUtil;

import java.util.Date;

public class CountdownActivity extends AppCompatActivity {

    private static final String countdownTimeFormat = "HH:mm:dd";
    private static TextView countdownText;
    private static Date endTime = null;
    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_countdown);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        countdownText = (TextView) findViewById(R.id.dialog_countdown_time);

        handler = new Handler();
        if (endTime == null) {
            endTime = MainActivity.getUser().getFbShareUnlockTime();
            handler.removeCallbacks(updateTimer);
            handler.postDelayed(updateTimer, 0);
        }
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            Date nowTime = new Date();
            if (endTime.after(nowTime)) {
                Log.d("FB Share Time", endTime + " vs " + nowTime);

                Long diff = endTime.getTime() - nowTime.getTime();

                int hour = (int) (diff / 1000) / 3600;
                int min = (int) (diff / 1000) / 60 % 60;
                int sec = (int) (diff / 1000) % 60;

                String hourStr = SystemUtil.addZeroFront(hour);
                String minStr = SystemUtil.addZeroFront(min);
                String secStr = SystemUtil.addZeroFront(sec);

                countdownText.setText(hourStr + ":" + minStr + ":" + secStr);
                handler.postDelayed(this, 1000);
            } else {
                finish();
            }
        }
    };

    public void clickOK(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimer);
        MainActivity.requestNewInterstitial();
    }
}
