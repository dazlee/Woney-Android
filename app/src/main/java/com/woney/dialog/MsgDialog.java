package com.woney.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.woney.R;
import com.woney.data.WoneyKey;

public class MsgDialog extends AppCompatActivity {

    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);

        textView = (TextView) findViewById(R.id.dialog_text);

        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString(WoneyKey.TEXT_KEY));
    }

    public void clickClose(View view) {
        finish();
    }
}
