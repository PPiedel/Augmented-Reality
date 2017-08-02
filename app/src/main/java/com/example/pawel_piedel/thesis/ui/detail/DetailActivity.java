package com.example.pawel_piedel.thesis.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.BusinessAdapter;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Category;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.categories_details)
    TextView categories;

    @BindView(R.id.distance_details)
    TextView distance;


    private Business business;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_details);

        setUnBinder(ButterKnife.bind(this));

        presenter.attachView(this);

        getBusinessFromIntent();

        presenter.onViewPrepared();

        setUpLayout();
    }


    private void setUpLayout() {
        checkNotNull(business);
        setUpToolbar();
        setUpRating();
        setUpReviewCount();
        setUpCategories();
        setUpDistance();

    }

    public void setUpTitle() {
        title.setText(String.format("%s", business.getName()));
    }

    public void setUpRating() {

        ratingBar.setRating((float) business.getRating());

        rating.setText(String.format("%s", business.getRating()));
    }

    public void setUpReviewCount() {
        review_count.setText(String.format("(%s)", business.getReviewCount()));
    }

    public void setUpCategories() {
        String categoryBuilder = "";
        for (Category cat : business.getCategories()) {
            categoryBuilder+=", "+cat.getTitle();
        }
        String category = categoryBuilder.substring(2,categoryBuilder.length()-1);
        categories.setText(category);
    }

    public void setUpDistance(){
        distance.setText(String.format("%.1f km", business.getDistance() / 1000));
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void getBusinessFromIntent() {
        business = (Business) getIntent().getSerializableExtra(BusinessAdapter.BUSINESS);
    }

    @Override
    public void showBusinessImage() {
        Glide.with(this)
                .load(business.getImageUrl())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
