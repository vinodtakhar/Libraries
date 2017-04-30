package com.phoneutils.crosspromotion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public class InterstitialBaseActivity extends RewardedVideoBaseActivity{
    private Handler adLoadingTimeHandler;
    private Runnable adLoadingTimeRunnable;

    private InterstitialAd mInterstitialAd;

    private boolean showInterstitialAdOnLoad = false;
    private int numberOfTryToLoadInterstitialAd = 5;
    private boolean showInterstitialAdOnceLoaded = false;
    public boolean keepInterstitialAdLoadedOnceClosed = false;
    private String loadingMessage = "Loading...";
    private int loadingTime = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adLoadingTimeHandler = new Handler(Looper.getMainLooper());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(getInterstitialAdUnitResourceId()));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                if(!isActivityPaused()) {
                    if (showInterstitialAdOnLoad) {
                        showInterstitialAdOnLoad = false;
                        showInterstitialAd(true);
                    } else if (showInterstitialAdOnceLoaded) {
                        showInterstitialAdOnceLoaded = false;
                        showInterstitialAd(true);
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                numberOfTryToLoadInterstitialAd--;

                if(numberOfTryToLoadInterstitialAd > 0){ //try  5 times
                    requestNewInterstitial();
                }
            }

            @Override
            public void onAdClosed() {
                if(keepInterstitialAdLoadedOnceClosed) {
                    requestNewInterstitial();
                }
            }
        });

        showInterstitialAdOnLoad = getShowInterstitialAdOnLoad();
        numberOfTryToLoadInterstitialAd = getNumberOfTryToLoadInterstitialAd();
        keepInterstitialAdLoadedOnceClosed = getKeepInterstitialAdLoadedOncePreviousClosed();
        loadingMessage = getAdLoadingMessage();
        loadingTime = getAdLoadingTime();

        if(isInterstitialAdNeedOnThisActivity()) {
            requestNewInterstitial();
        }
    }

    protected boolean isInterstitialAdNeedOnThisActivity(){
        return true;
    }

    private boolean isInterstitialAdLoaded(){
        return mInterstitialAd != null && mInterstitialAd.isLoaded();
    }
    private void showInterstitialAd(boolean showLoadingBeforeAd){
        if(isInterstitialAdLoaded()) {
            if(!showLoadingBeforeAd) {
                mInterstitialAd.show();
            }else{
                startLoadingAndShow();
            }
        }else{
            showInterstitialAdOnceLoaded = true;
        }
    }

    protected void showInterstitial(){
        showInterstitial(1,true);
    }

    protected void showInterstitial(boolean showLoadingBeforeAd){
        showInterstitial(1,showLoadingBeforeAd);
    }

    protected void showInterstitial(int showInEvery){
        showInterstitial(showInEvery,true);
    }

    protected void showInterstitial(int showInEvery,boolean showLoadingBeforeAd){
        showInterstitial(getClass().getName(),showInEvery,showLoadingBeforeAd);
    }

    protected void showInterstitial(String tag,int showInEvery){
        showInterstitial(tag,showInEvery,true);
    }

    protected void showInterstitial(final String tag,final int showInEvery,final boolean showLoadingBeforeAd){
        int counter = (int) AppPreferences.getLongSharedPreference(this,tag,0);
        if(counter % showInEvery == 0) {
            showInterstitialAd(showLoadingBeforeAd);
        }
        AppPreferences.setLongSharedPreference(this,tag,counter+1);
    }

//    protected void showInterstitial(final String tag,final int showInEvery,final Intent intentToLaunch){
//        int counter = (int) AppPreferences.getLongSharedPreference(this,tag,0);
//        if(counter % showInEvery == 0 && isInterstitialAdLoaded()) {
//
//        }else{
//            context.sta
//        }
//        AppPreferences.setLongSharedPreference(this,tag,counter+1);
//    }

    private void startLoadingAndShow(){
        showProgress(loadingMessage);

        adLoadingTimeRunnable = new Runnable() {
            @Override
            public void run() {
                showInterstitialAd(false);

                cancelTimeoutHandler();
            }
        };

        adLoadingTimeHandler.postDelayed(adLoadingTimeRunnable,loadingTime);
    }

    private void cancelTimeoutHandler(){
        hideProgress();

        if(adLoadingTimeRunnable !=null){
            adLoadingTimeHandler.removeCallbacks(adLoadingTimeRunnable);
            adLoadingTimeRunnable = null;
        }
    }

    private void requestNewInterstitial() {
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(getAdRequest());
        }
    }

    @Override
    protected void onResume() {
        boolean wasActivityPaused = isActivityPaused();
        super.onResume(); //as this will reset to non-paused

        if(wasActivityPaused){
            if((showInterstitialAdOnceLoaded || showInterstitialAdOnLoad)){
                showInterstitialAd(true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimeoutHandler();
    }

    public int getInterstitialAdUnitResourceId(){return R.string.ad_interstitial_unit_id;}

    protected boolean getShowInterstitialAdOnLoad() {
        return false;
    }

    protected int getNumberOfTryToLoadInterstitialAd() {
        return 5;
    }

    protected boolean getKeepInterstitialAdLoadedOncePreviousClosed() {
        return false;
    }

    protected String getAdLoadingMessage() {
        return "Loading";
    }

    public int getAdLoadingTime() {
        return 2000;
    }
}
