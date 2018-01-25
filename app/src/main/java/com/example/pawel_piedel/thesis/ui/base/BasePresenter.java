package com.example.pawel_piedel.thesis.ui.base;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BasePresenter<V extends BaseView> implements Presenter<V> {
    protected BusinessRepository businessRepository;
    protected LocationRepository locationRepository;
    protected AccessTokenRepository accessTokenRepository;
    protected V view;

    protected BasePresenter() {
    }

    @Inject
    protected BasePresenter(BusinessRepository businessRepository, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}
