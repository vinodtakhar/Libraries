package com.phoneutils.crosspromotion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author vinodtakhar
 * @version 1.0
 * @since 7/12/16
 */

public class BasePolicyActivity extends AppCompatActivity{
    protected Button btnAccept,btnDecline;
    protected TextView tvPolicy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_policy);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnDecline = (Button)findViewById(R.id.btnDecline);

        tvPolicy = (TextView) findViewById(R.id.tvPolicy);
    }

    public void onAccept(View view){

    }

    public void onDecline(View view){
        this.finish();
    }

    public void setContent(String policy){
        tvPolicy.setText(policy);
    }

    public void setAcceptTitle(String text){
        btnAccept.setText(text);
    }

    public void setDeclineTitle(String text){
        btnDecline.setText(text);
    }
}
