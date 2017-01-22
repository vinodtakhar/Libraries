package com.phoneutils.crosspromotion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by vinodtakhar on 22/01/17.
 */

public class CrossAdActivity extends BaseActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cross_fragment);

        findViewById(R.id.btnExit).setVisibility(View.GONE);

        linearLayout = (LinearLayout)findViewById(R.id.layout);

        CrossAdView crossAdView = new CrossAdView(this);
        crossAdView.setColumn(2);
        crossAdView.setOrientation(LinearLayoutManager.VERTICAL);

        if(crossAdView.hasCrossAds()) {
            linearLayout.addView(crossAdView.getView());
        }else{
            this.finish();
        }
    }
}
