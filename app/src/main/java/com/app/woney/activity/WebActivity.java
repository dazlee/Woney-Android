package com.app.woney.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.woney.R;
import com.app.woney.data.WoneyKey;

/**
 * Created by houan on 2017/1/16.
 */

public class WebActivity extends AppCompatActivity {

    private WebView webView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString(WoneyKey.WEB_URL_KEY);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
