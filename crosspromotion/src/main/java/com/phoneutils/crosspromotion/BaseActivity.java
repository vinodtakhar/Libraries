package com.phoneutils.crosspromotion;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
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
public class BaseActivity extends ActionBarActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String permissionBeingAsked;
    private int clientRequestCode;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                onInterstitialLoaded();
            }
        });

        requestNewInterstitial();
    }

    public void onInterstitialLoaded(){}

    protected void showInterstitial(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    protected void initBanner() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DC7C9FD46CD1CA86196555FA421470F7")
                .addTestDevice("75BCE6A3D40329AA644B7DA2D7241198").build();
        mAdView.loadAd(adRequest);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DC7C9FD46CD1CA86196555FA421470F7")
                .addTestDevice("75BCE6A3D40329AA644B7DA2D7241198")
                .build();

        mInterstitialAd.loadAd(adRequest);
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
            if(AppPreferences.getSharedPreference(this,AppPreferences.KEY_APPS_JSON)==null ||
                    (AppPreferences.getLongSharedPreference(this,AppPreferences.KEY_LAST_SYNC_TIME,0l) + AlarmManager.INTERVAL_DAY*7)<= System.currentTimeMillis()){
                AppsLoader.load(this);
            }
        }
    }

    protected void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(Utility.isConnected(this) && AppPreferences.getSharedPreference(this,AppPreferences.KEY_APPS_JSON)!=null) {
            CrossFragment dFragment = new CrossFragment();
            dFragment.show(getSupportFragmentManager(), "");
        }else{
            this.finish();
        }
    }
}
