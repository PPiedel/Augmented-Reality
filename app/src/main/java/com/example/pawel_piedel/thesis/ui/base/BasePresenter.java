package com.example.pawel_piedel.thesis.ui.base;

import com.example.pawel_piedel.thesis.data.BusinessDataSource;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BasePresenter<V extends BaseView> implements Presenter<V> {
    private BusinessDataSource businessDataSource;
    private V view;

    protected BasePresenter() {
    }

    @Inject
    protected BasePresenter(BusinessDataSource businessDataSource) {
        this.businessDataSource = businessDataSource;
    }


    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public V getView() {
        return view;
    }

    protected BusinessDataSource getBusinessDataSource() {
        return businessDataSource;
    }

}
