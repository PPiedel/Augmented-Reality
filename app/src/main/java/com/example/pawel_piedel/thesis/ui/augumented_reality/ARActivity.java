package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.os.Bundle;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ARActivity extends BaseActivity implements ARContract.View {

    @Inject
    ARPresenter<ARContract.View> arPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        arPresenter.attachView(this);

    }

    @Override
    public void showBusinessOnScreen(Business business) {

    }
}
