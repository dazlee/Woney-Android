package com.app.woney.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.woney.R;
import com.app.woney.data.WoneyKey;

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
