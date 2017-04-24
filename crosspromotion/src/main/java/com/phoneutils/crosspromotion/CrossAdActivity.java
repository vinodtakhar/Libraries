package com.phoneutils.crosspromotion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by vinodtakhar on 22/01/17.
 */

public class CrossAdActivity extends OldBaseActivity {
    public static final String EXTRA_COLUMN = "extra_column";
    public static final String EXTRA_SHOW_DESCRIPTION = "extra_show_description";
    private LinearLayout linearLayout;
    private int column = 2;
    private boolean showDescription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cross_fragment);

        findViewById(R.id.btnExit).setVisibility(View.GONE);

        linearLayout = (LinearLayout)findViewById(R.id.layout);

        if(getIntent()!=null){
            if(getIntent().hasExtra(EXTRA_COLUMN)){
                column = getIntent().getIntExtra(EXTRA_COLUMN, 2);
            }
            if(getIntent().hasExtra(EXTRA_SHOW_DESCRIPTION)){
                showDescription = getIntent().getBooleanExtra(EXTRA_SHOW_DESCRIPTION,false);
            }
        }

        CrossAdView crossAdView = new CrossAdView(this);
        crossAdView.setColumn(column);
        crossAdView.setShowDescription(showDescription);
        crossAdView.setOrientation(LinearLayoutManager.VERTICAL);

        if(crossAdView.hasCrossAds()) {
            linearLayout.addView(crossAdView.getView());
        }else{
            this.finish();
        }
    }
}
