package com.mango.libs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RelativeLayout;

import com.phoneutils.crosspromotion.BaseActivity;
import com.phoneutils.crosspromotion.CrossAdView;

public class MainActivity extends BaseActivity {

    CrossAdView crossAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);

        setShowCrossAds(true);
        setShowCrossActivity(true);
        startLoader("http://theorycrosspromotion.appspot.com/getapps","main");

        crossAdView = new CrossAdView(this);
        crossAdView.setAutoScroll(true);
        crossAdView.setOrientation(LinearLayoutManager.HORIZONTAL);

        layout.addView(crossAdView.getView());
    }

    @Override
    protected void onStop() {
        crossAdView.onDestroy();
        super.onStop();
    }
}
