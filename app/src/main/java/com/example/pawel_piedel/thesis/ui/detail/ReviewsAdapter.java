package com.example.pawel_piedel.thesis.ui.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 18.08.2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();
    private ViewHolder holder;
    private List<Review> reviews = new ArrayList<>();
    private Context context;

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Review review = reviews.get(position);
        if (!Objects.equals(review.getUser().getImageUrl(), "")) {
            Glide.with(context)
                    .load(review.getUser().getImageUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        holder.author.setText(review.getUser().getName());

        holder.review.setText(review.getText());

        holder.timestamp.setText(review.getTimeCreated());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        Log.d(LOG_TAG,"Jestem w set reviews");
        this.reviews.addAll(reviews);
        Log.d(LOG_TAG,""+reviews.size());
        Log.d(LOG_TAG,""+this.reviews.size());
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.author)
        TextView author;

        @BindView(R.id.review)
        TextView review;

        @BindView(R.id.profile_image)
        ImageView imageView;


        @BindView(R.id.timestamp)
        TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
