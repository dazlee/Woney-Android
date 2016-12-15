package com.woney.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.woney.R;
import com.woney.data.WoneyKey;

public class GainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gain);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        Integer gain = bundle.getInt(WoneyKey.GAIN_KEY);

        TextView dialogEarnText = (TextView) findViewById(R.id.dialog_earn_text);
        dialogEarnText.setText(getString(R.string.dialog_woney_reward, gain));
    }

    public void clickCollect(View view) {
        finish();
    }
}
