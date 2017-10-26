package com.example.pawel_piedel.thesis.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {
    public final static String BUSINESS = "business";
    private final String LOG_TAG = BusinessAdapter.class.getName();
    private final List<Business> businessList = new ArrayList<>();
    private Context context;


    public BusinessAdapter() {
    }


    public void setBusinessList(List<Business> businessList) {
        this.businessList.clear();
        this.businessList.addAll(businessList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);

        Business business = businessList.get(position);
        if (!Objects.equals(business.getImageUrl(), "")) {
            // Log.v(LOG_TAG,""+business.getName()+" "+business.getImageUrl());
            Glide.with(context)
                    .load(business.getImageUrl())
                    .centerCrop()
                    .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
        }

        holder.title.setText(business.getName());
        holder.address1.setText((String.valueOf(business.getLocation().getAddress1())));
        holder.address2.setText(String.format("%s %s", business.getLocation().getZipCode(), business.getLocation().getCity()));
        holder.rating.setText(String.valueOf(business.getRating()));
        holder.distance.setText(String.format("%.1f km", business.getDistance() / 1000));
       /* if (sharedPreferences.getBoolean(business.getId(),true)){
            holder.favouriteImage.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thubnail)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.address)
        TextView address1;
        @BindView(R.id.address2)
        TextView address2;
        @BindView(R.id.rating)
        TextView rating;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.favourite)
        ImageView favouriteImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(int position) {
            setOnItemClickListener(position);
        }

        private void setOnItemClickListener(int position) {
            itemView.setOnClickListener(view -> {
                Log.d(LOG_TAG, "" + position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(BUSINESS, businessList.get(position));
                context.startActivity(intent);
            });
        }


    }


}
