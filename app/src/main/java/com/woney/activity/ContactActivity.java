package com.woney.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.woney.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    public void sendMail(View view) {
        Uri emailUri = Uri.parse("mailto:" + getString(R.string.dialog_mail));
        Intent intent = new Intent(Intent.ACTION_SENDTO, emailUri);
        startActivity(intent);
    }

    public void clickClose() {
        finish();
    }
}
