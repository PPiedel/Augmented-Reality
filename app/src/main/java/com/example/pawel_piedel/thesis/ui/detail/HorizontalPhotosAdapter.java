package com.example.pawel_piedel.thesis.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pawel_piedel.thesis.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Pawel_Piedel on 17.08.2017.
 */

public class HorizontalPhotosAdapter extends RecyclerView.Adapter<HorizontalPhotosAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<String> imageUrls;

    public HorizontalPhotosAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent, false);

        if (itemView.getLayoutParams().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.onBind(position);

        Glide.with(context)
                .load(imageUrls.get(position))
                .centerCrop()
                .crossFade()
                .bitmapTransform(new RoundedCornersTransformation(context,2,2))
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

        void onBind(int position) {
            setOnItemClickListener(position);
        }

        private void setOnItemClickListener(int position) {
            itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imageUrls);
                bundle.putInt("position", position);

                FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                SlideShowDialogFragment newFragment = SlideShowDialogFragment.newInstance();
                newFragment.setCancelable(true);
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

            });
        }
    }
}