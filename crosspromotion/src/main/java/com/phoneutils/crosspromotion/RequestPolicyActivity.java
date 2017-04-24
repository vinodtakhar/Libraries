package com.phoneutils.crosspromotion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author vinodtakhar
 * @version 1.0
 * @since 7/12/16
 */

public abstract class RequestPolicyActivity extends AppCompatActivity{
    protected Button btnContinue;
    protected TextView tvTitle,tvLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppPreferences.getBooleanSharedPreference(this,AppPreferences.KEY_POLICY_STATUS,false)){
            startActivity(new Intent(this,getNextActivity()));
            this.finish();
        }
        else if(getResources().getInteger(R.integer.activity_orientation)!=getRequestedOrientation()){
            this.setRequestedOrientation(getResources().getInteger(R.integer.activity_orientation));
        }

        setContentView(R.layout.activity_request_policy);

        btnContinue = (Button)findViewById(R.id.btnAccept);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLink = (TextView) findViewById(R.id.tvLink);

        tvTitle.setText("Welcome to "+getResources().getString(R.string.app_name));

        String html = "<b>By Continuing You are Agree with <u><i>Privacy Policy and Terms and Conditions</i></u></b>";

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
        startActivity(new Intent(this,getNextActivity()));
        this.finish();
    }

    public abstract Class getNextActivity();
}
