package com.example.pawel_piedel.thesis.main.tabs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public class BussinessAdapter extends RecyclerView.Adapter<BussinessAdapter.ViewHolder> {

    private List<Business> businessList;

    BussinessAdapter(List<Business> businesses) {
        this.businessList = businesses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Business business = businessList.get(position);
        holder.firstLine.setText(business.getName());
        holder.secondLine.setText(business.getPhone());
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.firstLine) TextView firstLine;
        @BindView(R.id.secondLine) TextView secondLine;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
