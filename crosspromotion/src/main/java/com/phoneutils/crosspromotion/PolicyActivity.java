package com.phoneutils.crosspromotion;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author vinodtakhar
 * @version 1.0
 * @since 20/12/16
 */

public class PolicyActivity extends OldBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        WebView webView = (WebView)findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                showProgress("Loading...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                hideProgress();
            }
        });

        webView.loadUrl(getResources().getString(R.string.policy_url));
    }
}
