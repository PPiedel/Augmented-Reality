package com.example.pawel_piedel.thesis.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

public class DetailActivity extends BaseActivity implements DetailContract.View {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Inject
    DetailPresenter<DetailContract.View> presenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_details);

        setUnBinder(ButterKnife.bind(this));

        presenter.attachView(this);

        setUpLayout();
    }


    private void setUpLayout() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
