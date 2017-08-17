package com.example.pawel_piedel.thesis.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pawel_piedel.thesis.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 17.08.2017.
 */

public class HorizontalPhotosAdapter extends RecyclerView.Adapter<HorizontalPhotosAdapter.MyViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public HorizontalPhotosAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        if (itemView.getLayoutParams().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(context)
                .load(imageUrls.get(position))
                .centerCrop()
                .crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.horizontal_item_view_image)
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}