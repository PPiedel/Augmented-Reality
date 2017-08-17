package com.example.pawel_piedel.thesis.ui.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

public class DetailActivity extends BaseActivity implements DetailContract.View {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Inject
    DetailPresenter<DetailContract.View> presenter;

    @BindView(R.id.collapsing_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.business_image)
    ImageView imageView;

    @BindView(R.id.title_details)
    TextView title;

    @BindView(R.id.rating_bar_details)
    RatingBar ratingBar;

    @BindView(R.id.rating_details)
    TextView rating;

    @BindView(R.id.review_count_details)
    TextView review_count;

    @BindView(R.id.zipcode_details)
    TextView address;

    @BindView(R.id.distance_details)
    TextView distance;

    @BindView(R.id.street_details)
    TextView street;

    @BindView(R.id.horizontal_recycler_view)
    RecyclerView horizontalRecyclerView;

    private HorizontalPhotosAdapter horizontalAdapter;

    private Business newBusiness;

    private String businessId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_details);

        setUnBinder(ButterKnife.bind(this));

        presenter.attachView(this);

        getOldBusinessFromIntent();

        setUpToolbar();

        presenter.onViewPrepared(businessId);

    }

    @Override
    public void showBusinessDetails(Business business) {
        showBusinessImage(business);

        title.setText(String.format("%s", business.getName()));

        ratingBar.setRating((float) business.getRating());
        rating.setText(String.format("%s", business.getRating()));

        review_count.setText(String.format("(%s)", business.getReviewCount()));

        street.setText(business.getLocation().getAddress1());

        address.setText(String.format("%s %s", business.getLocation().getZipCode(), business.getLocation().getCity()));

        distance.setText(String.format("%.1f km", business.getDistance() / 1000));

        setUpRecyclerView(business);
    }

    public void showBusinessImage(Business business) {
        Glide.with(this)
                .load(business.getImageUrl())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void showOldBusiness() {
        Business business = (Business) getIntent().getSerializableExtra(BusinessAdapter.BUSINESS);
        Log.d(LOG_TAG,business.toString());
        showBusinessDetails(business);
    }

    @Override
    @OnClick(R.id.call_action)
    public void onCallButtonClicked() {
        presenter.makeCall(newBusiness);
    }

    @Override
    @OnClick(R.id.website_action)
    public void onWebsiteButtonclicked() {
        presenter.goToWebsite(newBusiness);
    }

    @Override
    @OnClick(R.id.favourite_action)
    public void onFavouriteButtonClicked() {
        presenter.addToFavourite(newBusiness);
    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpRecyclerView(Business business){
        if ( business.getPhotos()!=null && !business.getPhotos().isEmpty()){
            horizontalAdapter=new HorizontalPhotosAdapter(this, new ArrayList<>(business.getPhotos()));
            horizontalRecyclerView.setAdapter(horizontalAdapter);
            horizontalAdapter.notifyDataSetChanged();

            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
            horizontalRecyclerView.setAdapter(horizontalAdapter);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void getOldBusinessFromIntent() {
       Business business = (Business) getIntent().getSerializableExtra(BusinessAdapter.BUSINESS);
       businessId = business.getId();
        Log.d(LOG_TAG,"Business id : "+businessId);
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }


    @Override
    public void setUpBusiness(Business business) {
        this.newBusiness = business;
    }
}
