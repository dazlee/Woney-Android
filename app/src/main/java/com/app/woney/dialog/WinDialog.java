package com.app.woney.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.app.woney.R;
import com.app.woney.data.OngoingData;

public class WinDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_win);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        TextView announceTime = (TextView) findViewById(R.id.winner_first_series_time);
        TextView winnerTitleTextView = (TextView) findViewById(R.id.winner_title);
        OngoingData ongoingData = OngoingData.getOngoingData();
        if (ongoingData != null) {
            winnerTitleTextView.setVisibility(View.VISIBLE);
            announceTime.setText(ongoingData.getFormatFirstDraw());
        } else {
            winnerTitleTextView.setVisibility(View.INVISIBLE);
            announceTime.setText("Loading winner information...");
        }
    }

    public void clickOK(View view) {
        finish();
    }
}
