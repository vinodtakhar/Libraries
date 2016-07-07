package com.phoneutils.crosspromotion;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by vinod on 24/11/15.
 */
public class CrossFragment extends DialogFragment {

    private Button btnExit;
    private RecyclerView recyclerView;
    private AppsAdapter appsAdapter;
    private ArrayList<AppModel> apps = null;

    public CrossFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cross_fragment, container,
                false);

        btnExit = (Button)rootView.findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        int column = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE?3:2;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),column));

        appsAdapter = new AppsAdapter();

        ResponseModel responseModel = new Gson().fromJson(AppPreferences.getSharedPreference(getContext(),AppPreferences.KEY_APPS_JSON),ResponseModel.class);

        if(responseModel==null || responseModel.getApps()==null || responseModel.getApps().size()==0){
            AppPreferences.setLongSharedPreference(getContext(),AppPreferences.KEY_LAST_SYNC_TIME,0l);
            getActivity().finish();
        }else{
            apps = responseModel.getApps();
        }

        recyclerView.setAdapter(appsAdapter);

        return rootView;
    }

    public void onResume()
    {
        super.onResume();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Window window = getDialog().getWindow();
        window.setLayout((int)(displayMetrics.widthPixels - displayMetrics.widthPixels*.20), (int)(displayMetrics.heightPixels-displayMetrics.heightPixels*.20));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void launchApp(String appPackageName){

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (Exception anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item,null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvTitle.setText(apps.get(position).getTitle());
            holder.tvDescription.setText(apps.get(position).getDescription());
            Glide.with(getContext()).load(apps.get(position).getLogo()).override(600,200).into(holder.ivLogo);
            holder.packageName = apps.get(position).getPackageName();
        }

        @Override
        public int getItemCount() {
            return apps==null?0:apps.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tvTitle;
            TextView tvDescription;
            ImageView ivLogo;
            String packageName;

            public ViewHolder(View itemView) {
                super(itemView);

                tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
                tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
                ivLogo = (ImageView) itemView.findViewById(R.id.ivIcon);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                launchApp(packageName);
            }
        }
    }
}