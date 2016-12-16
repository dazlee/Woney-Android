package com.woney.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.woney.R;
import com.woney.data.OngoingData;
import com.woney.fragment.EarnMainFragment;

public class WinDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_win);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        TextView announceTime = (TextView) findViewById(R.id.winner_first_series_time);
        OngoingData ongoingData = OngoingData.getOngoingData();
        announceTime.setText(ongoingData.getFormatFirstDraw());
    }

    public void clickOK(View view) {
        finish();
    }
}
