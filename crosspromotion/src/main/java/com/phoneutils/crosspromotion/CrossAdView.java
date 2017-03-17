package com.phoneutils.crosspromotion;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by vinodtakhar on 22/01/17.
 */

public class CrossAdView {
    private Context context;
    private View view;

    private RecyclerView recyclerView;
    private TextView tvTitle;
    private AppsAdapter appsAdapter;
    private ArrayList<AppModel> apps = null;
    private boolean showDescription = false;
    private boolean autoScroll = false;
    private int scrollInterval = 2000;
    private int scrollCount = 1;

    private Runnable autoScrollRunnable;

    private int orientation = LinearLayoutManager.VERTICAL;

    private int column = 2;

    public CrossAdView(Context context){
        this.context = context;

        view = LayoutInflater.from(context).inflate(R.layout.recycler_view,null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tvTitle = (TextView) view.findViewById(R.id.tvMessage);

        ResponseModel responseModel = new Gson().fromJson(AppPreferences.getSharedPreference(context,AppPreferences.KEY_APPS_JSON),ResponseModel.class);

        if(responseModel==null || responseModel.getApps()==null || responseModel.getApps().size()==0){
            AppPreferences.setLongSharedPreference(context,AppPreferences.KEY_LAST_SYNC_TIME,0l);
            view.setVisibility(View.GONE);
        }else{
            apps = responseModel.getApps();
        }
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public void setTitleVisibility(int visibility){
        tvTitle.setVisibility(visibility);
    }

    public boolean hasCrossAds(){
        return view.getVisibility() == View.VISIBLE;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public View getView(){

        int layoutId = R.layout.app_item;

        if(orientation == LinearLayoutManager.VERTICAL) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, column));
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            layoutId = R.layout.app_item_horizontal;
        }
        appsAdapter = new AppsAdapter(context,apps,layoutId,showDescription);
        recyclerView.setAdapter(appsAdapter);

        if(isAutoScroll() && orientation == LinearLayoutManager.HORIZONTAL && apps!=null && apps.size()>0){
            autoScrollRunnable = new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(context,"scroll:"+scrollCount,Toast.LENGTH_LONG).show();
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    layoutManager.setSmoothScrollbarEnabled(true);
                    layoutManager.scrollToPosition(scrollCount++ %
                            layoutManager.getItemCount());

                    recyclerView.postDelayed(autoScrollRunnable,scrollInterval);
                }
            };

            recyclerView.postDelayed(autoScrollRunnable,scrollInterval);
        }

        return view;
    }

    public void onDestroy(){
        if(autoScrollRunnable!=null){
            recyclerView.removeCallbacks(autoScrollRunnable);
        }
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public int getScrollInterval() {
        return scrollInterval;
    }

    public void setScrollInterval(int scrollInterval) {
        this.scrollInterval = scrollInterval;
    }
}
