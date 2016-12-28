package com.app.woney.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.woney.R;
import com.app.woney.data.WoneyKey;
import com.app.woney.util.ScreenUtil;
import com.app.woney.util.SystemUtil;

public class MsgDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);

        setupView();
    }

    public void setupView() {
        TextView textView = (TextView) findViewById(R.id.dialog_text);
        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString(WoneyKey.TEXT_KEY));
    }

    public void clickOK(View view) {
        finish();
    }
}
