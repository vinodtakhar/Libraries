package com.phoneutils.crosspromotion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by vinodtakhar on 22/01/17.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<AppModel> apps = null;
    private int itemLayoutId;
    private boolean showDescription = false;

    public AppsAdapter(Context context,ArrayList<AppModel> apps,int itemLayoutId,boolean showDescription){
        this.context = context;
        this.apps = apps;
        this.itemLayoutId = itemLayoutId;
        this.showDescription = showDescription;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(apps.get(position).getTitle());
        holder.tvDescription.setText(apps.get(position).getDescription());
        Glide.with(context).load(apps.get(position).getLogo()).override(600,200).into(holder.ivLogo);
        holder.packageName = apps.get(position).getPackageName();

        if(context.getResources().getString(R.string.show_cross_app_animation).equals("YES")) {
            Animation scaleAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setRepeatMode(2);
            holder.itemView.clearAnimation();
            holder.itemView.startAnimation(scaleAnim);
        }
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

            tvDescription.setVisibility(showDescription?View.VISIBLE:View.GONE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            launchApp(packageName);
        }
    }

    private void launchApp(String appPackageName){

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (Exception anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
