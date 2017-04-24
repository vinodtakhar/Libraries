package com.phoneutils.crosspromotion;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public class BaseAdActivity extends BaseActivity {
    private ArrayList<String> testDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, getResources().getString(R.string.ad_app_id));

        testDevices.add("DC7C9FD46CD1CA86196555FA421470F7");
        testDevices.add("7E97A11C1B1F4804F656ED363496314B");
        testDevices.add("F0D10D62E523166E0FD28927292F8A4F");
        testDevices.add("75BCE6A3D40329AA644B7DA2D7241198");
    }

    public void addTestDevice(String deviceId){
        testDevices.add(deviceId);
    }

    public void setTestDevices(ArrayList<String> devices){
        this.testDevices = new ArrayList<>(devices);
    }

    public void clearAllTestDevices(){
        testDevices.clear();
    }

    public AdRequest.Builder getAdRequestBuilder(){
        AdRequest.Builder builder = new AdRequest.Builder();

        for(String testDevice: testDevices){
            builder.addTestDevice(testDevice);
        }

        return builder;
    }

    public AdRequest getAdRequest(){
        return getAdRequestBuilder().build();
    }
}
