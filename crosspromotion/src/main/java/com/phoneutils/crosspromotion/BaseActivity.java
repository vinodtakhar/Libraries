package com.phoneutils.crosspromotion;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by vinodtakhar on 28/4/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String TAG = BaseActivity.class.getName();
    private String permissionBeingAsked;
    private int clientRequestCode;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog progressDialog;
    private AdView mAdView;
    private boolean showCrossAds = false;
    private int column;
    private boolean showCrossActivity = false;
    private boolean showCrossAdDescription;
    private Handler adLoadingTimeoutHandler;
    private Runnable adLoadingTimeoutRunnable;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(getResources().getInteger(R.integer.activity_orientation));

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        adLoadingTimeoutHandler = new Handler(Looper.getMainLooper());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                cancelTimeoutHandler();

                showFullAd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                cancelTimeoutHandler();
            }
        });

        if(shouldShowAdOnLoad()) {
            requestNewInterstitial();
        }
    }

    @Override
    protected void onPause() {
        cancelTimeoutHandler();
        super.onPause();
    }

    public boolean shouldShowAdOnLoad(){
        return false;
    }

    public int getAdLoadingTimeout(){
        return 7000;
    }

    public String getAdLoadingMessage(){
        return "Loading...";
    }

    public void setShowCrossAdDescription(boolean showCrossAdDescription) {
        this.showCrossAdDescription = showCrossAdDescription;
    }

    public void setShowCrossActivity(boolean showCrossActivity) {
        this.showCrossActivity = showCrossActivity;
    }

    protected void setShowCrossAds(boolean showCrossAds){
        this.showCrossAds = showCrossAds;
        column = getResources().getInteger(R.integer.activity_orientation)==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE? 3:2;
    }

    protected void setShowCrossAds(boolean showCrossAds,int column){
        this.showCrossAds = showCrossAds;
        this.column = column;
    }

    private void showFullAd(){
        mInterstitialAd.show();
    }

    protected void showInterstitial(){
        requestNewInterstitial();
    }

    protected void showInterstitial(int showInEvery){
        int counter = (int)AppPreferences.getLongSharedPreference(this,getClass().getName(),0);

        if(counter%showInEvery==0) {
            AppPreferences.setLongSharedPreference(this,getClass().getName(),counter+1);
            requestNewInterstitial();
        }
    }

    protected void initBanner() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DC7C9FD46CD1CA86196555FA421470F7")
                .addTestDevice("7E97A11C1B1F4804F656ED363496314B")
                .addTestDevice("75BCE6A3D40329AA644B7DA2D7241198").build();
        mAdView.loadAd(adRequest);
    }

    private void startTimeoutHandler(){
        adLoadingTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                cancelTimeoutHandler();
            }
        };

        adLoadingTimeoutHandler.postDelayed(adLoadingTimeoutRunnable,getAdLoadingTimeout());
    }

    private void cancelTimeoutHandler(){
        hideProgress();

        if(adLoadingTimeoutRunnable!=null){
            adLoadingTimeoutHandler.removeCallbacks(adLoadingTimeoutRunnable);
            adLoadingTimeoutRunnable = null;
        }
    }

    private void requestNewInterstitial() {
        showProgress(getAdLoadingMessage());

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DC7C9FD46CD1CA86196555FA421470F7")
                .addTestDevice("7E97A11C1B1F4804F656ED363496314B")
                .addTestDevice("75BCE6A3D40329AA644B7DA2D7241198")
                .build();

        mInterstitialAd.loadAd(adRequest);

        startTimeoutHandler();
    }

    protected void requestPermission(int requestCode,String permission) {

        permissionBeingAsked = permission;
        clientRequestCode = requestCode;

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission) || !AppPreferences.getBooleanSharedPreference(this, permission, false)) {
                    AppPreferences.setBooleanSharedPreference(this, permission, true); /*set  that we have already asked the permission to handle rational*/

                    ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            PERMISSIONS_REQUEST_CODE);
                } else
                    Utility.showPermissionRequiredDialog(this, "Permission Required", "Please grant required permissions in Application Settings under Permissions", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utility.openAppSettings(BaseActivity.this);
                        }
                    });
            } else {
                onPermissionGranted(clientRequestCode, permissionBeingAsked);
            }
        }else {
            onPermissionGranted(clientRequestCode, permissionBeingAsked);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    onPermissionGranted(clientRequestCode,permissionBeingAsked);
                else
                    onPermissionDenied(clientRequestCode,permissionBeingAsked);
                break;
        }
    }

    public void onPermissionGranted(int requestCode,String grantedPermission) {
    }

    public void onPermissionDenied(int requestCode,String deniedPermission) {
    }

    protected void hideProgress() {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    protected void showProgress(String s) {
        progressDialog = ProgressDialog.show(this,"",s,true,false);
    }

    public void startLoader(String url, String category){
        AppPreferences.setSharedPreference(this,AppPreferences.KEY_URL,url);
        AppPreferences.setSharedPreference(this,AppPreferences.KEY_CATEGORY,category);

        if(Utility.isConnected(this)){
            boolean isJsonNull = AppPreferences.getSharedPreference(this,AppPreferences.KEY_APPS_JSON)==null;
            boolean isItOneWeek = (AppPreferences.getLongSharedPreference(this,AppPreferences.KEY_LAST_SYNC_TIME,0l) + (AlarmManager.INTERVAL_DAY * 7))<= System.currentTimeMillis();

            if(isJsonNull ||
                    isItOneWeek){
                AppsLoader.load(this);
            }
        }
    }

    protected void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        boolean isJsonNull = AppPreferences.getSharedPreference(this,AppPreferences.KEY_APPS_JSON)==null;

        if(showCrossAds && Utility.isConnected(this) && !isJsonNull) {
            if(showCrossActivity){
                startActivity(new Intent(this,CrossAdActivity.class).putExtra(CrossAdActivity.EXTRA_COLUMN,column)
                    .putExtra(CrossAdActivity.EXTRA_SHOW_DESCRIPTION,showCrossAdDescription));
                this.finish();
            }else {
                CrossFragment dFragment = new CrossFragment();
                dFragment.setColumn(column);
                dFragment.setShowDescription(showCrossAdDescription);
                dFragment.show(getSupportFragmentManager(), "");
            }
        }else{
            this.finish();
        }
    }
}
