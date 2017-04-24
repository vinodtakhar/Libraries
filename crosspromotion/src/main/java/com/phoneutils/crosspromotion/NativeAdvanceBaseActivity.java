package com.phoneutils.crosspromotion;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public class NativeAdvanceBaseActivity extends InterstitialBaseActivity{
    private boolean requestAppInstallAds = true;
    private boolean requestContentAds = true;

    private int numberOfTryToLoadNativeAdvanceAd = 5;

    private int nativeAdPlaceHolderResourceId;

    public void setNativeAdPlaceHolderResourceId(int nativeAdPlaceHolderResourceId) {
        this.nativeAdPlaceHolderResourceId = nativeAdPlaceHolderResourceId;
    }

    public void setNumberOfTryToLoadNativeAdvanceAd(int numberOfTryToLoadNativeAdvanceAd) {
        this.numberOfTryToLoadNativeAdvanceAd = numberOfTryToLoadNativeAdvanceAd;
    }

    public void setRequestAppInstallAds(boolean requestAppInstallAds) {
        if(!requestAppInstallAds && !requestContentAds){
            showToast("Both App Install and Content Ads can not be disabled!");
        }else {
            this.requestAppInstallAds = requestAppInstallAds;
        }
    }

    public void setRequestContentAds(boolean requestContentAds) {
        if(!requestAppInstallAds && !requestContentAds){
            showToast("Both App Install and Content Ads can not be disabled!");
        }else {
            this.requestContentAds = requestContentAds;
        }
    }

    protected int getNativeAdvanceAdUnitResourceId(){
        return R.string.ad_native_advance_unit_id;
    }

    private void loadNativeAdvanceAd() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(getNativeAdvanceAdUnitResourceId()));

        if (requestAppInstallAds) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                    FrameLayout frameLayout = (FrameLayout) findViewById(nativeAdPlaceHolderResourceId);
                    NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater().inflate(R.layout.ad_app_install, null);
                    populateAppInstallAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    FrameLayout frameLayout = (FrameLayout) findViewById(nativeAdPlaceHolderResourceId);
                    NativeContentAdView adView = (NativeContentAdView) getLayoutInflater().inflate(R.layout.ad_content, null);
                    populateContentAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                numberOfTryToLoadNativeAdvanceAd--;

                if(numberOfTryToLoadNativeAdvanceAd>0){
                    loadNativeAdvanceAd();
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

//                loadNativeAdvanceAd();
            }
        }).build();

        adLoader.loadAd(getAdRequest());
    }

    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView adView) {

        VideoController vc = nativeAppInstallAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();

//                loadNativeAdvanceAd();
            }
        });


        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // The MediaView will display a video asset if one is present in the ad, and the first image
        // asset otherwise.
        MediaView mediaView = (MediaView) adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

        if (vc.hasVideoContent()) {
        }

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    private void populateContentAdView(NativeContentAd nativeContentAd, NativeContentAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }
}
