package com.phoneutils.crosspromotion;

import com.google.android.gms.ads.AdView;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public class BannerBaseActivity extends InterstitialBaseActivity{
    private AdView mAdView;

    protected void initBanner() {
        mAdView = (AdView) findViewById(getBannerAdViewResourceId());

        mAdView.loadAd(getAdRequest());
    }

    protected int getBannerAdViewResourceId(){
        return R.id.adView;
    }
}
