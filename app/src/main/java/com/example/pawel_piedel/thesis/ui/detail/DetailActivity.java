package com.example.pawel_piedel.thesis.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.BusinessAdapter;
import com.example.pawel_piedel.thesis.data.model.Business;
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
        setUpToolbar();

    }

    public void setUpTitle() {
        checkNotNull(business);
        mToolbar.setTitle(business.getName());
        //businessTitle.setText(business.getName());
    }


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);
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
