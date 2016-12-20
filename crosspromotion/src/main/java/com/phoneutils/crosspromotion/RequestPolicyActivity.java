package com.phoneutils.crosspromotion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author vinodtakhar
 * @version 1.0
 * @since 7/12/16
 */

public class RequestPolicyActivity extends BaseActivity{
    protected Button btnContinue;
    protected TextView tvTitle,tvLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_policy);

        btnContinue = (Button)findViewById(R.id.btnAccept);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLink = (TextView) findViewById(R.id.tvLink);

        if(AppPreferences.getBooleanSharedPreference(this,AppPreferences.KEY_POLICY_STATUS,false)){
            onContinue(btnContinue);
        }

        tvTitle.setText("Welcome to "+getResources().getString(R.string.app_name));

        String html = "<b>By Continuing You are Agree with <u><i>Terms and Conditions</i></u></b>";

        tvLink.setText(Html.fromHtml(html));

        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestPolicyActivity.this,PolicyActivity.class));
            }
        });
    }

    public void onContinue(View view){
        AppPreferences.setBooleanSharedPreference(this,AppPreferences.KEY_POLICY_STATUS,true);
    }
}
