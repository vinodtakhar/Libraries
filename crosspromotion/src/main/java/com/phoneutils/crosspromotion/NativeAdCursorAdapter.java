package com.phoneutils.crosspromotion;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;

/**
 * Created by vinodtakhar on 22/04/17.
 */

public abstract class NativeAdCursorAdapter<VH extends NativeAdCursorAdapter.ViewHolder> extends RecyclerViewAdapter<VH> {
    private static final String TAG = "NativeAdCursorAdapter";

    protected static final int VIEW_TYPE_NORMAL = 0;
    protected static final int VIEW_TYPE_NATIVE_AD = 1;

    private ArrayList<String> testDevices = new ArrayList<>();
    private ArrayList<NativeExpressAdView> nativeExpressAdViews = new ArrayList<>();

    private Context context;
    private final float scale;

    private RecyclerView recyclerView;

    public NativeAdCursorAdapter(Context context, RecyclerView recyclerView, Cursor cursor) {
        super(context, cursor);

        this.context = context;
        scale = context.getResources().getDisplayMetrics().density;
        this.recyclerView = recyclerView;

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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View nativeExpressLayoutView = LayoutInflater.from(parent.getContext()).inflate(getNativeAdViewLayoutResourceId(),parent,false);
        return (VH) new NativeExpressAdViewHolder(nativeExpressLayoutView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(!(holder instanceof NativeExpressAdViewHolder)){
            Log.e(TAG,"Not an NativeAdView");
            super.onBindViewHolder(holder,getNormalViewPosition(position));
            return;
        }

        NativeExpressAdViewHolder nativeExpressHolder = (NativeExpressAdViewHolder) holder;

        NativeExpressAdView adView = getNativeAdView((position/getShowAtModulusPosition()));

        ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;

        if (adCardView.getChildCount() > 0) {
            adCardView.removeAllViews();
        }
        if (adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }

        adCardView.addView(adView);
    }

    protected int getNativeAdHeight(){
        return 150;
    }

    protected int getNativeAdWidth() {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        return w - w/10;
    }

    protected int getWidthOfView(View view){
        return (int)((view.getWidth() - view.getPaddingLeft() - view.getPaddingRight())/scale);
    }

    protected int getNativeAdUnitResourceId(){
        return R.string.ad_native_unit_id;
    }

    @Override
    public int getCount() {
        int adCount = super.getCount()/getShowAdPerItems();

        if(showAdFromTop() && super.getCount()%getShowAdPerItems()!=0){
            adCount += 1;
        }

        if(nativeExpressAdViews.size()==0) {
            addNativeExpressAds(adCount);
        }

        int totalCount  = super.getCount() + adCount;

        return totalCount;
    }

    @Override
    public long getItemId(int position) {
        if(position!=0 && position%getShowAdPerItems()==0){
            return 0;
        }else {
            return super.getItemId(position - position % getShowAdPerItems());
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(showAdFromTop()) {
            if(position==0) {
                return VIEW_TYPE_NATIVE_AD;
            }else{
                return position % getShowAtModulusPosition() == 0 ? VIEW_TYPE_NATIVE_AD : VIEW_TYPE_NORMAL;
            }
        }else {
            if(position==0){
                return VIEW_TYPE_NORMAL;
            }else{
                return (position+1) % getShowAtModulusPosition() == 0 ? VIEW_TYPE_NATIVE_AD : VIEW_TYPE_NORMAL;
            }
        }
    }

    protected boolean showAdFromTop(){
        return false;
    }

    public boolean isThisAdView(int position){
        return getItemViewType(position) == VIEW_TYPE_NATIVE_AD;
    }

    protected int getNormalViewPosition(int position){
        return position - (position/getShowAtModulusPosition()) - (showAdFromTop()?1:0);
    }

    private int getShowAtModulusPosition(){
        return getShowAdPerItems() + 1;
    }

    protected int getShowAdPerItems(){
        return 12;
    }

    protected int getNativeAdViewLayoutResourceId() {
        return R.layout.ad_adapter_item;
    }

    public static class NativeExpressAdViewHolder extends NativeAdCursorAdapter.ViewHolder {
        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    //related to loading native ads
    private NativeExpressAdView getNativeAdView(int position){
        if(position<0 || position>=nativeExpressAdViews.size()){
            position = 0;
        }

        return nativeExpressAdViews.get(position);
    }
    private void addNativeExpressAds(int adCount) {
        for (int i = 0; i <= adCount; i++) {
            final NativeExpressAdView adView = new NativeExpressAdView(context);
            nativeExpressAdViews.add(adView);

            if(keepHiddenUntilNativeAdLoaded()) {
                adView.setVisibility(View.GONE);
            }
        }

        setUpAndLoadNativeExpressAds(adCount);
    }

    private void setUpAndLoadNativeExpressAds(final int adCount) {

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= adCount; i++) {
                    final NativeExpressAdView adView = getNativeAdView(i);
                    AdSize adSize = new AdSize(getNativeAdWidth(), getNativeAdHeight());
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(context.getResources().getString(getNativeAdUnitResourceId()));
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(0);
            }
        });
    }

    protected boolean keepHiddenUntilNativeAdLoaded(){
        return true;
    }

    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= nativeExpressAdViews.size()) {
            return;
        }

        final NativeExpressAdView adView = nativeExpressAdViews.get(index);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(keepHiddenUntilNativeAdLoaded()) {
                    getNativeAdView(index).setVisibility(View.VISIBLE);
                }
                loadNativeExpressAd(index + 1);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadNativeExpressAd(index + 1);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(getAdRequest());
    }
}
