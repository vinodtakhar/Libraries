package com.phoneutils.crosspromotion;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public abstract class NativeExpressBaseActivity extends InterstitialBaseActivity {
    private NativeExpressAdView mAdView;
    private int numberOfTryToLoadNativeExpressAd = 5;
    private boolean adLoadingFailed;

    protected NativeExpressAdView getNativeAdView(){
        return (NativeExpressAdView) findViewById(getNativeAdViewResourceId());
    }

    protected void initNativeAd() {
        mAdView = getNativeAdView();

        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                if(--numberOfTryToLoadNativeExpressAd>0 && !NativeExpressBaseActivity.this.isActivityPaused()) {
                    mAdView.loadAd(getAdRequest());
                }else{
                    adLoadingFailed = true;
                }
            }
        });

        numberOfTryToLoadNativeExpressAd = getNumberOfTryToLoadNativeExpressAd();

        mAdView.loadAd(getAdRequest());
    }

    protected int getNativeAdViewResourceId(){
        return R.id.nativeAdView;
    }

    @Override
    protected void onResume() {
        boolean wasPaused = isActivityPaused();
        super.onResume();

        if(wasPaused && adLoadingFailed){
            mAdView.loadAd(getAdRequest());
        }
    }

    protected int getNumberOfTryToLoadNativeExpressAd() {
        return 5;
    }
}
