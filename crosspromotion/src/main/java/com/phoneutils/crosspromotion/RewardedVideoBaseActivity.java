package com.phoneutils.crosspromotion;

/**
 * Created by vinodtakhar on 22/04/17.
 */


import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by vinodtakhar on 17/03/17.
 */

public class RewardedVideoBaseActivity extends BaseAdActivity implements RewardedVideoAdListener {
    private static final String TAG = RewardedVideoBaseActivity.class.getName();

    private RewardedVideoAd mVideoAd;
    private boolean keepVideoAdLoadedOnceClosed = false;

    private int numberOfTryToLoadVideoAd = 3;

    private View rewardedVideoAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mVideoAd.setRewardedVideoAdListener(this);

        if(isVideoAdNeedOnThisActivity()) {
            requestNewVideoAd();
        }
    }

    protected boolean isVideoAdNeedOnThisActivity(){
        return false;
    }

    public void setRewardedVideoAdView(View rewardedVideoAdView) {
        this.rewardedVideoAdView = rewardedVideoAdView;
    }

    public void setNumberOfTryToLoadVideoAd(int numberOfTryToLoadVideoAd) {
        this.numberOfTryToLoadVideoAd = numberOfTryToLoadVideoAd;
    }

    public void setKeepVideoAdLoadedOnceClosed(boolean keepVideoAdLoadedOnceClosed) {
        this.keepVideoAdLoadedOnceClosed = keepVideoAdLoadedOnceClosed;
    }

    protected int getRewardedVideoAdUnitResourceId(){
        return R.string.ad_rewarded_video_unit_id;
    }

    private void requestNewVideoAd() {
        mVideoAd.loadAd(getResources().getString(getRewardedVideoAdUnitResourceId()),getAdRequest());
    }

    protected void showRewardedVideo(){
        if(mVideoAd.isLoaded()) {
            mVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        rewardedVideoAdView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onRewardedVideoAdOpened() {}
    @Override
    public void onRewardedVideoStarted() {}
    @Override
    public void onRewardedVideoAdClosed() {
        rewardedVideoAdView.setVisibility(View.GONE);

        if(keepVideoAdLoadedOnceClosed){
            requestNewVideoAd();
        }
    }
    @Override
    public void onRewarded(RewardItem rewardItem) {}
    @Override
    public void onRewardedVideoAdLeftApplication() {}
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if(--numberOfTryToLoadVideoAd>0){
            requestNewVideoAd();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoAd.pause(this);
    }

//    @Override
//    public void onDestroy() {
//        mVideoAd.destroy(this);
//        super.onDestroy();
//    }
}

