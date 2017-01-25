package com.phoneutils.crosspromotion;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by vinod on 24/11/15.
 */
public class CrossFragment extends DialogFragment {

    private static final String TAG = CrossFragment.class.getName();
    private Button btnExit;
    private int column;
    private LinearLayout layout;
    private boolean showDescription = false;

    public CrossFragment() {
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cross_fragment, container,
                false);

        btnExit = (Button)rootView.findViewById(R.id.btnExit);
        layout = (LinearLayout)rootView.findViewById(R.id.layout);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        CrossAdView crossAdView = new CrossAdView(getContext());
        crossAdView.setColumn(column);
        crossAdView.setShowDescription(showDescription);
        crossAdView.setOrientation(LinearLayoutManager.VERTICAL);

        if(!crossAdView.hasCrossAds()){
            getActivity().finish();
        }else{
            layout.addView(crossAdView.getView());
        }

        return rootView;
    }

    public void onResume()
    {
        super.onResume();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Window window = getDialog().getWindow();
        window.setLayout((int)(displayMetrics.widthPixels - displayMetrics.widthPixels*.10), (int)(displayMetrics.heightPixels-displayMetrics.heightPixels*.10));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}