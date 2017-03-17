package com.phoneutils.crosspromotion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by vinodtakhar on 17/03/17.
 */

public class BaseActivityWithRewardedVideo extends BaseActivity implements RewardedVideoAdListener{
    private static final String TAG = BaseActivityWithRewardedVideo.class.getName();
    private RewardedVideoAd mVideoAd;
    private Handler videoLoadingTimeoutHandler;
    private Runnable videoLoadingTimeoutRunnable;

    public BaseActivityWithRewardedVideo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mVideoAd.setRewardedVideoAdListener(this);

        videoLoadingTimeoutHandler = new Handler(Looper.getMainLooper());
    }

    private void startVideoAdTimeoutHandler(){
        videoLoadingTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                cancelVideoAdTimeoutHandler();
            }
        };

        videoLoadingTimeoutHandler.postDelayed(videoLoadingTimeoutRunnable,getAdLoadingTimeout());
    }

    private void cancelVideoAdTimeoutHandler(){
        hideProgress();

        if(videoLoadingTimeoutRunnable!=null){
            videoLoadingTimeoutHandler.removeCallbacks(videoLoadingTimeoutRunnable);
            videoLoadingTimeoutRunnable = null;
        }
    }

    private void requestNewVideoAd() {
        showProgress(getAdLoadingMessage());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DC7C9FD46CD1CA86196555FA421470F7")
                .addTestDevice("7E97A11C1B1F4804F656ED363496314B")
                .addTestDevice("75BCE6A3D40329AA644B7DA2D7241198")
                .addTestDevice("F0D10D62E523166E0FD28927292F8A4F")
                .build();

        mVideoAd.loadAd(getResources().getString(R.string.rewarded_video_id),adRequest);

        startVideoAdTimeoutHandler();
    }

    protected void showRewardedVideo(){
        requestNewVideoAd();
    }

    private void showVideoAd(){
        if(videoLoadingTimeoutRunnable!=null) {
            mVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        showVideoAd();
        cancelVideoAdTimeoutHandler();
    }

    @Override
    public void onRewardedVideoAdOpened() {}
    @Override
    public void onRewardedVideoStarted() {}
    @Override
    public void onRewardedVideoAdClosed() {}
    @Override
    public void onRewarded(RewardItem rewardItem) {}
    @Override
    public void onRewardedVideoAdLeftApplication() {}
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        cancelVideoAdTimeoutHandler();
    }

    @Override
    public void onResume() {
        mVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mVideoAd.pause(this);
        cancelVideoAdTimeoutHandler();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mVideoAd.destroy(this);
        super.onDestroy();
    }

}
