package com.example.pawel_piedel.thesis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {

    private List<Business> businessList = new ArrayList<>();
    private Context context;

    public BusinessAdapter() {
    }

    public void addBusinessList(List<Business> businessList){
        this.businessList.addAll(businessList);
        notifyDataSetChanged();
    }

    public void addBusiness(Business business){
        this.businessList.add(business);
        notifyDataSetChanged();
    }

    public void clear() {
        this.businessList.clear();
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
        Business business = businessList.get(position);
        Glide.with(context)
                .load(business.getImageUrl())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        holder.title.setText(business.getName());
        holder.rating.setText((String.valueOf(business.getRating())));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thubnail) ImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.genre) TextView genre;
        @BindView(R.id.releaseYear) TextView year;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
